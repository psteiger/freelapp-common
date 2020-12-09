package com.freelapp.common.domain.getdatatimelines

import com.freelapp.common.entity.item.ItemWithStats
import com.freelapp.common.entity.Timeline
import kotlinx.coroutines.flow.StateFlow

fun interface GetDataTimelinesUseCase<DataTypeWithStats : ItemWithStats> {

    operator fun invoke(): Map<Timeline, StateFlow<List<DataTypeWithStats>>>
}