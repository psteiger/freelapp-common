package com.freelapp.common.domain.getnearbyusers

import com.freelapp.common.entity.item.Item
import com.freelapp.common.entity.Key
import com.freelapp.common.entity.User
import kotlinx.coroutines.flow.SharedFlow

fun interface GetNearbyUsersUseCase<UserType, DataType> where UserType : User<UserType, DataType>,
                                                              DataType : Item {

    operator fun invoke(): SharedFlow<Map<Key, UserType>>
}