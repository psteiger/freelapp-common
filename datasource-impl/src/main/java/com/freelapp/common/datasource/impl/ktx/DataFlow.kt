package com.freelapp.common.datasource.impl.ktx

import com.freelapp.common.entity.Key
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class DataFlow constructor(
    private val query: Query,
    config: (Query.() -> Unit)? = null
) {

    constructor(
        path: String,
        config: (Query.() -> Unit)? = null
    ) : this(
        FirebaseDatabase.getInstance().reference.child(path),
        config
    )

    init {
        config?.invoke(query)
    }

    val singleValue: Flow<DataSnapshot?>
        get() = flow { emit(query.getSnapshot()) }

    @ExperimentalCoroutinesApi
    val value: Flow<DataSnapshot>
        get() = query
            .asDataSnapshotFlow()
            .flowOn(Dispatchers.IO)

    @ExperimentalCoroutinesApi
    val children: Flow<Map<Key, DataSnapshot>>
        get() = query
            .childrenAsFlow()
            .flowOn(Dispatchers.IO)
}