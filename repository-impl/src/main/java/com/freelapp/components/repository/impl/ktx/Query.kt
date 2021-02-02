package com.freelapp.components.repository.impl.ktx

import com.freelapp.common.application.ktx.tryOffer
import com.freelapp.common.entity.Key
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.Query
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun Query.addValueEventListener(block: ValueEventListenerBuilder.() -> Unit) =
    addValueEventListener(ValueEventListener(block))

fun Query.addChildEventListener(block: ChildEventListenerBuilder.() -> Unit) =
    addChildEventListener(ChildEventListener(block))

fun Query.addListenerForSingleValueEvent(block: ValueEventListenerBuilder.() -> Unit) =
    addListenerForSingleValueEvent(ValueEventListener(block))

@ExperimentalCoroutinesApi
fun Query.asDataSnapshotFlow(): Flow<DataSnapshot> = callbackFlow {
    val listener = addValueEventListener {
        onDataChange { tryOffer(this) }
        onCancelled { cancel(CancellationException("API Error", toException())) }
    }
    awaitClose { removeEventListener(listener) }
}

@ExperimentalCoroutinesApi
fun Query.childrenAsFlow(): Flow<Map<Key, DataSnapshot>> = callbackFlow {
    val map = mutableMapOf<Key, DataSnapshot>()
    val listener = addChildEventListener {
        onChildAdded {
            key?.let {
                map[it] = this
                tryOffer(map.toMap())
            }
        }
        onChildChanged {
            key?.let {
                map[it] = this
                tryOffer(map.toMap())
            }
        }
        onChildRemoved {
            key?.let {
                map.remove(it)
                tryOffer(map.toMap())
            }
        }
        onCancelled { cancel(CancellationException("API Error", toException())) }
    }
    awaitClose { removeEventListener(listener) }
}