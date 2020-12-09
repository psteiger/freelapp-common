package com.freelapp.common.application.getlocationname

import com.freelapp.common.application.entity.Latitude
import com.freelapp.common.application.entity.Longitude

interface GetLocationName {
    suspend operator fun invoke(
        location: Pair<Latitude, Longitude>,
        radius: Int
    ): String?
}