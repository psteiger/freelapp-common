package com.freelapp.common.datasource.impl.ktx

import com.freelapp.common.application.ktx.tryOffer
import com.freelapp.common.entity.Key
import com.google.firebase.database.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume

class ChildEventListenerBuilder(
    private var onChildAdded: ((DataSnapshot, String?) -> Unit)? = null,
    private var onChildChanged: ((DataSnapshot, String?) -> Unit)? = null,
    private var onChildMoved: ((DataSnapshot, String?) -> Unit)? = null,
    private var onChildRemoved: ((DataSnapshot) -> Unit)? = null,
    private var onCancelled: ((DatabaseError) -> Unit)? = null
) {
    fun onChildAdded(onChildAdded: DataSnapshot.(String?) -> Unit) =
        apply { this.onChildAdded = onChildAdded }
    fun onChildChanged(onChildChanged: DataSnapshot.(String?) -> Unit) =
        apply { this.onChildChanged = onChildChanged }
    fun onChildMoved(onChildMoved: DataSnapshot.(String?) -> Unit) =
        apply { this.onChildMoved = onChildMoved }
    fun onChildRemoved(onChildRemoved: DataSnapshot.() -> Unit) =
        apply { this.onChildRemoved = onChildRemoved }
    fun onCancelled(onCancelled: DatabaseError.() -> Unit) =
        apply { this.onCancelled = onCancelled }

    fun build() = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            this@ChildEventListenerBuilder.onChildAdded?.invoke(snapshot, previousChildName)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            this@ChildEventListenerBuilder.onChildChanged?.invoke(snapshot, previousChildName)
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            this@ChildEventListenerBuilder.onChildMoved?.invoke(snapshot, previousChildName)
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            this@ChildEventListenerBuilder.onChildRemoved?.invoke(snapshot)
        }

        override fun onCancelled(snapshot: DatabaseError) {
            this@ChildEventListenerBuilder.onCancelled?.invoke(snapshot)
        }
    }
}

class ValueEventListenerBuilder(
    private var onDataChange: ((DataSnapshot) -> Unit)? = null,
    private var onCancelled: ((DatabaseError) -> Unit)? = null
) {
    fun onDataChange(onDataChange: DataSnapshot.() -> Unit) =
        apply { this.onDataChange = onDataChange }
    fun onCancelled(onCancelled: DatabaseError.() -> Unit) =
        apply { this.onCancelled = onCancelled }

    fun build() = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            this@ValueEventListenerBuilder.onDataChange?.invoke(snapshot)
        }

        override fun onCancelled(error: DatabaseError) {
            this@ValueEventListenerBuilder.onCancelled?.invoke(error)
        }
    }
}

@Suppress("FunctionName")
internal fun ValueEventListener(block: ValueEventListenerBuilder.() -> Unit) =
    ValueEventListenerBuilder().apply(block).build()

@Suppress("FunctionName")
internal fun ChildEventListener(block: ChildEventListenerBuilder.() -> Unit) =
    ChildEventListenerBuilder().apply(block).build()

inline fun <reified T : Any> DataSnapshot.getTypedValue(): T =
    getValue(object : GenericTypeIndicator<T>() {})!!

suspend inline fun Query.getSnapshot() = withContext(Dispatchers.IO) {
    suspendCancellableCoroutine<DataSnapshot?> { continuation ->
        addListenerForSingleValueEvent {
            onDataChange { continuation.resume(this) }
            onCancelled { continuation.resume(null) }
        }
    }
}