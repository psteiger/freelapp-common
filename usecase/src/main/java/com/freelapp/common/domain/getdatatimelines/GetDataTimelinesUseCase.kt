package com.freelapp.common.domain.getdatatimelines

import com.freelapp.common.entity.Item
import com.freelapp.common.entity.Timeline
import kotlinx.coroutines.flow.SharedFlow

interface GetDataTimelinesUseCase<DataType> where DataType : Item<DataType> {

    operator fun invoke(): Map<Timeline, SharedFlow<List<DataType>>>
}