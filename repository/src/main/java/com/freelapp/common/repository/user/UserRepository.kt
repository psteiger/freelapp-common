package com.freelapp.common.repository.user

import com.freelapp.common.entity.DataOwner
import com.freelapp.common.entity.Data
import com.freelapp.common.entity.Key
import com.freelapp.common.entity.Mode
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface UserRepository<Owner, DataType> where Owner : DataOwner<Owner, DataType>,
                                                DataType : Data<DataType> {

    val globalUsers: SharedFlow<Map<Key, Owner>>
    val nearbyUsers: SharedFlow<Map<Key, Owner>>
    val searchRadius: StateFlow<Int>
    val searchMode: StateFlow<Mode>
    val searchText: StateFlow<String>
    val hideShowOwnData: StateFlow<Boolean>

    fun setSearchMode(mode: Mode)
    fun setSearchFilter(query: String)
    fun setSearchRadius(radius: Int)
    fun setHideShowOwnData(show: Boolean)
}