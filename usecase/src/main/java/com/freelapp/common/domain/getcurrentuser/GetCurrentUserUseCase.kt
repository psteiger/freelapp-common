package com.freelapp.common.domain.getcurrentuser

import com.freelapp.common.entity.Item
import com.freelapp.common.entity.User
import kotlinx.coroutines.flow.StateFlow

fun interface GetCurrentUserUseCase<UserType, DataType> where UserType : User<UserType, DataType>,
                                                          DataType : Item<DataType> {

    operator fun invoke(): StateFlow<UserType?>
}