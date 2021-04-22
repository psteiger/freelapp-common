package com.freelapp.common.repository.user

import com.freelapp.common.entity.*
import com.freelapp.common.entity.item.Item
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface UserRepository<UserType, DataType> where UserType : User<DataType>,
                                                   DataType : Item {

    val globalUsers: SharedFlow<Map<Key, UserType>>
    val nearbyUsers: SharedFlow<Map<Key, UserType>>
    val globalUsersPositions: SharedFlow<Map<Key, Pair<Latitude, Longitude>>>
    val searchRadius: StateFlow<Int>
    val searchMode: StateFlow<SearchMode>
    val searchText: StateFlow<String>
    val hideShowOwnData: StateFlow<Boolean>

    fun setSearchMode(searchMode: SearchMode)
    fun setSearchFilter(query: String)
    fun setSearchRadius(radius: Int)
    fun setHideShowOwnData(show: Boolean)

    fun queryAtLocation(location: Pair<Latitude, Longitude>, radius: Int)
}