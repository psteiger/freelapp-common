package com.freelapp.common.domain.getglobaluserspositions

import com.freelapp.common.entity.Key
import com.freelapp.common.entity.Latitude
import com.freelapp.common.entity.Longitude
import kotlinx.coroutines.flow.StateFlow

interface GetGlobalUsersPositionsUseCase {

    operator fun invoke(): StateFlow<Map<Key, Pair<Latitude, Longitude>>>
}