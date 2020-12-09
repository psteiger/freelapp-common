package com.freelapp.common.repository.user

import com.freelapp.common.entity.*
import kotlinx.coroutines.flow.StateFlow

interface UserRepository<UserType, DataType> where UserType : User<UserType, DataType>,
                                                   DataType : Item<DataType> {

    val globalUsers: StateFlow<Map<Key, UserType>>
    val nearbyUsers: StateFlow<Map<Key, UserType>>
    val globalUsersPositions: StateFlow<Map<Key, Pair<Latitude, Longitude>>>
    val searchRadius: StateFlow<Int>
    val searchMode: StateFlow<SearchMode>
    val searchText: StateFlow<String>
    val hideShowOwnData: StateFlow<Boolean>

    fun setSearchMode(searchMode: SearchMode)
    fun setSearchFilter(query: String)
    fun setSearchRadius(radius: Int)
    fun setHideShowOwnData(show: Boolean)
}