package com.freelapp.common.datasource

import com.freelapp.common.entity.Key
import com.freelapp.common.entity.Latitude
import com.freelapp.common.entity.Longitude
import com.freelapp.common.entity.User
import com.freelapp.common.entity.item.Item
import kotlinx.coroutines.flow.SharedFlow

interface NearbyUsersDataSource<UserType, DataType> where UserType : User<DataType>,
                                                          DataType : Item {
    val globalUsers: SharedFlow<Map<Key, UserType>>
    val nearbyUsers: SharedFlow<Map<Key, UserType>>
    val globalUsersPositions: SharedFlow<Map<Key, Pair<Latitude, Longitude>>>
    fun queryAtLocation(location: Pair<Latitude, Longitude>, radius: Int)
}