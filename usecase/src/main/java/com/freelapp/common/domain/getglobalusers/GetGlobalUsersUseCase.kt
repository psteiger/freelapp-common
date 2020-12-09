package com.freelapp.common.domain.getglobalusers

import com.freelapp.common.entity.item.Item
import com.freelapp.common.entity.Key
import com.freelapp.common.entity.User
import kotlinx.coroutines.flow.StateFlow

fun interface GetGlobalUsersUseCase<UserType, DataType> where UserType : User<UserType, DataType>,
                                                              DataType : Item {

    operator fun invoke(): StateFlow<Map<Key, UserType>>
}