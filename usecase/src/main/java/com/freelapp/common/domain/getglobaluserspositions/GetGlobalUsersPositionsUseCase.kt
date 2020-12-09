package com.freelapp.common.domain.getglobaluserspositions

import com.freelapp.common.entity.Key
import com.freelapp.common.entity.Latitude
import com.freelapp.common.entity.Longitude
import kotlinx.coroutines.flow.SharedFlow

interface GetGlobalUsersPositionsUseCase {

    operator fun invoke(): SharedFlow<Map<Key, Pair<Latitude, Longitude>>>
}