package com.freelapp.common.repository.currentuser

import com.freelapp.common.entity.item.Item
import com.freelapp.common.entity.User
import kotlinx.coroutines.flow.SharedFlow

interface CurrentUserRepository<UserType, DataType> where UserType : User<UserType, DataType>,
                                                          DataType : Item {
    val user: SharedFlow<UserType?>
}