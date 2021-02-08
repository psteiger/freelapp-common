package com.freelapp.components.repository.impl

import com.freelapp.common.datasource.CurrentUserDataSource
import com.freelapp.common.entity.User
import com.freelapp.common.entity.item.Item
import com.freelapp.common.repository.currentuser.CurrentUserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

open class CurrentUserRepositoryImpl<UserType, DataType> @Inject constructor(
    scope: CoroutineScope,
    currentUserDataSource: CurrentUserDataSource<UserType, DataType>,
) : CurrentUserRepository<UserType, DataType> where UserType : User<DataType>,
                                                    DataType : Item {

    override val user: StateFlow<UserType?> =
        currentUserDataSource
            .user
            .stateIn(scope, SharingStarted.WhileSubscribed(), null)
}