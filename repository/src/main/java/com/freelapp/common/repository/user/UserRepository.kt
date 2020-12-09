package com.freelapp.common.repository.user

import com.freelapp.common.entity.*
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface UserRepository<UserType, DataType> where UserType : User<UserType, DataType>,
                                                   DataType : Item<DataType> {

    val globalUsers: SharedFlow<Map<Key, UserType>>
    val nearbyUsers: SharedFlow<Map<Key, UserType>>
    val globalUsersPositions: SharedFlow<Map<Key, Pair<Latitude, Longitude>>>
    val searchRadius: StateFlow<Int>
    val searchMode: StateFlow<Mode>
    val searchText: StateFlow<String>
    val hideShowOwnData: StateFlow<Boolean>

    fun setSearchMode(mode: Mode)
    fun setSearchFilter(query: String)
    fun setSearchRadius(radius: Int)
    fun setHideShowOwnData(show: Boolean)
}