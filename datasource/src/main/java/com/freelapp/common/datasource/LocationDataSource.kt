package com.freelapp.common.datasource

import com.freelapp.common.entity.Latitude
import com.freelapp.common.entity.Longitude
import kotlinx.coroutines.flow.SharedFlow

interface LocationDataSource {
    val location: SharedFlow<Pair<Latitude, Longitude>>
}