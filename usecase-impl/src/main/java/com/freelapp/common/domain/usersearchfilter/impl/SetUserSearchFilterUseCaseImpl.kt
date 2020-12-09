package com.freelapp.common.domain.usersearchfilter.impl

import com.freelapp.common.domain.usersearchfilter.SetUserSearchFilterUseCase
import com.freelapp.common.entity.User
import com.freelapp.common.entity.Item
import com.freelapp.common.repository.user.UserRepository

class SetUserSearchFilterUseCaseImpl<UserType, DataType>(
    private val userRepository: UserRepository<UserType, DataType>
) : SetUserSearchFilterUseCase where UserType : User<UserType, DataType>,
                                     DataType : Item<DataType> {

    override fun invoke(query: String) {
        userRepository.setSearchFilter(query)
    }
}