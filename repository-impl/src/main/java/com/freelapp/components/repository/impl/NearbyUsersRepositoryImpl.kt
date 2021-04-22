package com.freelapp.components.repository.impl

import com.freelapp.common.application.updatelocation.LifecycleCollector
import com.freelapp.common.datasource.LocationDataSource
import com.freelapp.common.datasource.NearbyUsersDataSource
import com.freelapp.common.datasource.OptionsDataSource
import com.freelapp.common.entity.*
import com.freelapp.common.entity.item.Item
import com.freelapp.common.repository.user.UserRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

open class NearbyUsersRepositoryImpl<UserType, DataType> @Inject constructor(
    lifecycleCollector: LifecycleCollector,
    locationDataSource: LocationDataSource,
    private val optionsDataSource: OptionsDataSource,
    private val nearbyUsersDataSource: NearbyUsersDataSource<UserType, DataType>
) : UserRepository<UserType, DataType> where UserType : User<DataType>,
                                             DataType : Item {

    override val globalUsers: SharedFlow<Map<Key, UserType>>
        get() = nearbyUsersDataSource.globalUsers

    override val nearbyUsers: SharedFlow<Map<Key, UserType>>
        get() = nearbyUsersDataSource.nearbyUsers

    override val globalUsersPositions: SharedFlow<Map<Key, Pair<Latitude, Longitude>>>
        get() = nearbyUsersDataSource.globalUsersPositions

    override val searchRadius: StateFlow<Int>
        get() = optionsDataSource.searchRadius

    override val searchMode: StateFlow<SearchMode>
        get() = optionsDataSource.searchMode

    override val searchText: StateFlow<String>
        get() = optionsDataSource.searchText

    override val hideShowOwnData: StateFlow<Boolean>
        get() = optionsDataSource.hideShowOwnData

    override fun setSearchMode(searchMode: SearchMode) {
        optionsDataSource.setSearchMode(searchMode)
    }

    override fun setSearchFilter(query: String) {
        optionsDataSource.setSearchFilter(query)
    }

    override fun setSearchRadius(radius: Int) {
        optionsDataSource.setSearchRadius(radius)
    }

    override fun setHideShowOwnData(show: Boolean) {
        optionsDataSource.setHideShowOwnData(show)
    }

    override fun queryAtLocation(location: Pair<Latitude, Longitude>, radius: Int) {
        nearbyUsersDataSource.queryAtLocation(location, radius)
    }

    init {
        val flow = combine(locationDataSource.location, optionsDataSource.searchRadius) { loc, r ->
            nearbyUsersDataSource.queryAtLocation(loc, r)
        }
        lifecycleCollector(flow)
    }
}