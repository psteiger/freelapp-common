package com.freelapp.common.domain.usersearchmode.impl

import com.freelapp.common.domain.usersearchmode.SetUserSearchModeUseCase
import com.freelapp.common.entity.Item
import com.freelapp.common.entity.SearchMode
import com.freelapp.common.entity.User
import com.freelapp.common.repository.user.UserRepository

class SetUserSearchModeUseCaseImpl<UserType, DataType>(
    private val userRepository: UserRepository<UserType, DataType>
) : SetUserSearchModeUseCase where UserType : User<UserType, DataType>,
                                   DataType : Item<DataType> {

    override fun invoke(searchMode: SearchMode) {
        userRepository.setSearchMode(searchMode)
    }
}