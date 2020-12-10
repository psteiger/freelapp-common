package com.freelapp.common.domain.getglobalusers

import com.freelapp.common.entity.item.Item
import com.freelapp.common.entity.Key
import com.freelapp.common.entity.User
import kotlinx.coroutines.flow.SharedFlow

fun interface GetGlobalUsersUseCase<UserType, DataType> where UserType : User<DataType>,
                                                              DataType : Item {

    operator fun invoke(): SharedFlow<Map<Key, UserType>>
}