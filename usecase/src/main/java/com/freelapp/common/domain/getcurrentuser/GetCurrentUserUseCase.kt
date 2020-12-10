package com.freelapp.common.domain.getcurrentuser

import com.freelapp.common.entity.item.Item
import com.freelapp.common.entity.User
import kotlinx.coroutines.flow.SharedFlow

fun interface GetCurrentUserUseCase<UserType, DataType> where UserType : User<UserType, DataType>,
                                                              DataType : Item {

    operator fun invoke(): SharedFlow<UserType?>
}