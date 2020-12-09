package com.freelapp.common.domain.getallusers

import com.freelapp.common.entity.Item
import com.freelapp.common.entity.Key
import com.freelapp.common.entity.User
import kotlinx.coroutines.flow.Flow

interface GetAllUsersUseCase<UserType, DataType> where UserType : User<UserType, DataType>,
                                                       DataType : Item<DataType> {

    operator fun invoke(): Flow<Map<Key, UserType>>
}