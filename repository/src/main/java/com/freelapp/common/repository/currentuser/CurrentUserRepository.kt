package com.freelapp.common.repository.currentuser

import com.freelapp.common.entity.User
import com.freelapp.common.entity.Item
import kotlinx.coroutines.flow.StateFlow

interface CurrentUserRepository<UserType, DataType> where UserType : User<UserType, DataType>,
                                                          DataType : Item<DataType> {
    val user: StateFlow<UserType?>
}