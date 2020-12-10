package com.freelapp.common.domain.getallusers

import com.freelapp.common.entity.item.Item
import com.freelapp.common.entity.Key
import com.freelapp.common.entity.User
import kotlinx.coroutines.flow.Flow

fun interface GetAllUsersUseCase<UserType, DataType> where UserType : User<DataType>,
                                                           DataType : Item {

    operator fun invoke(): Flow<Map<Key, UserType>>
}