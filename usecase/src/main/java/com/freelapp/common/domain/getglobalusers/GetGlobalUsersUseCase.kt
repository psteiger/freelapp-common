package com.freelapp.common.domain.getglobalusers

import com.freelapp.common.entity.Item
import com.freelapp.common.entity.Key
import com.freelapp.common.entity.User
import kotlinx.coroutines.flow.SharedFlow

interface GetGlobalUsersUseCase<UserType, DataType> where UserType : User<UserType, DataType>,
                                                          DataType : Item<DataType> {

    operator fun invoke(): SharedFlow<Map<Key, UserType>>
}