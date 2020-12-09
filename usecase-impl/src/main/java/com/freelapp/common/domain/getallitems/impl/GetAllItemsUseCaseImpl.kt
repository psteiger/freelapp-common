package com.freelapp.common.domain.getallitems.impl

import com.freelapp.common.domain.getallitems.GetAllItemsUseCase
import com.freelapp.common.domain.getallusers.GetAllUsersUseCase
import com.freelapp.common.entity.Item
import com.freelapp.common.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest

class GetAllItemsUseCaseImpl<UserType, DataType>(
    private val getAllUsersUseCase: GetAllUsersUseCase<UserType, DataType>
) : GetAllItemsUseCase<DataType> where UserType : User<UserType, DataType>,
                                       DataType : Item<DataType> {

    @ExperimentalCoroutinesApi
    override fun invoke(): Flow<List<DataType>> =
        getAllUsersUseCase()
            .mapLatest { userMap ->
                userMap
                    .values
                    .flatMap { it.data }
            }
            .flowOn(Dispatchers.Default)
}