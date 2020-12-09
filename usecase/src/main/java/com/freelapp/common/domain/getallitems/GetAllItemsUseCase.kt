package com.freelapp.common.domain.getallitems

import com.freelapp.common.entity.item.Item
import kotlinx.coroutines.flow.Flow

fun interface GetAllItemsUseCase<DataType> where DataType : Item {
    operator fun invoke(): Flow<List<DataType>>
}