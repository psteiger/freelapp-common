package com.freelapp.common.domain.getallitems

import com.freelapp.common.entity.Item
import kotlinx.coroutines.flow.Flow

interface GetAllItemsUseCase<DataType> where DataType : Item<DataType> {
    operator fun invoke(): Flow<List<DataType>>
}