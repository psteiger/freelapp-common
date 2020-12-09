package com.freelapp.common.domain.getdatatimelines

import com.freelapp.common.entity.Data
import com.freelapp.common.entity.Timeline
import kotlinx.coroutines.flow.SharedFlow

interface GetDataTimelines<DataType> where DataType : Data<DataType> {

    operator fun invoke(): Map<Timeline, SharedFlow<List<DataType>>>
}