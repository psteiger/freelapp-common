package com.freelapp.common.repository.user

import com.freelapp.common.entity.User
import com.freelapp.common.entity.Item
import com.freelapp.common.entity.Key
import com.freelapp.common.entity.Mode
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface UserRepository<UserType, DataType> where UserType : User<UserType, DataType>,
                                                   DataType : Item<DataType> {

    val globalUsers: SharedFlow<Map<Key, UserType>>
    val nearbyUsers: SharedFlow<Map<Key, UserType>>
    val searchRadius: StateFlow<Int>
    val searchMode: StateFlow<Mode>
    val searchText: StateFlow<String>
    val hideShowOwnData: StateFlow<Boolean>

    fun setSearchMode(mode: Mode)
    fun setSearchFilter(query: String)
    fun setSearchRadius(radius: Int)
    fun setHideShowOwnData(show: Boolean)
}