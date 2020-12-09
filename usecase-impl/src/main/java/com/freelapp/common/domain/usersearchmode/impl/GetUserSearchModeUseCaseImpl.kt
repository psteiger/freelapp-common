package com.freelapp.common.domain.usersearchmode.impl

import com.freelapp.common.domain.usersearchmode.GetUserSearchModeUseCase
import com.freelapp.common.entity.User
import com.freelapp.common.entity.Item
import com.freelapp.common.entity.Mode
import com.freelapp.common.repository.user.UserRepository
import kotlinx.coroutines.flow.StateFlow

class GetUserSearchModeUseCaseImpl<UserType, DataType>(
    private val userRepository: UserRepository<UserType, DataType>
) : GetUserSearchModeUseCase where UserType : User<UserType, DataType>,
                                   DataType : Item<DataType> {

    override fun invoke(): StateFlow<Mode> = userRepository.searchMode
}