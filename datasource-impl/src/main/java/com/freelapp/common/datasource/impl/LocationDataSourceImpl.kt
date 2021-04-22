package com.freelapp.common.datasource.impl

import com.freelapp.common.datasource.LocationDataSource
import com.freelapp.common.entity.Latitude
import com.freelapp.common.entity.Longitude
import com.freelapp.libs.locationfetcher.LocationSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class LocationDataSourceImpl @Inject constructor(
    locationSource: LocationSource,
    scope: CoroutineScope
) : LocationDataSource {

    @ExperimentalCoroutinesApi
    override val location: SharedFlow<Pair<Latitude, Longitude>> =
        locationSource.location
            .filterNotNull()
            .mapLatest { it.latitude to it.longitude }
            .shareIn(scope, SharingStarted.WhileSubscribed(), 1)
}