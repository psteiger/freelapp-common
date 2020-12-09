package com.freelapp.common.repository.currentuser

import com.freelapp.common.entity.item.Item
import com.freelapp.common.entity.User
import kotlinx.coroutines.flow.StateFlow

interface CurrentUserRepository<UserType, DataType> where UserType : User<UserType, DataType>,
                                                          DataType : Item {
    val user: StateFlow<UserType?>
}