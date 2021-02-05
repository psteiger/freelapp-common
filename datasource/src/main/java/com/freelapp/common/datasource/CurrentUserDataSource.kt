package com.freelapp.common.datasource

import com.freelapp.common.entity.User
import com.freelapp.common.entity.item.Item
import kotlinx.coroutines.flow.StateFlow

interface CurrentUserDataSource<UserType, DataType> where UserType : User<DataType>,
                                                          DataType : Item {
    val user: StateFlow<UserType?>
}