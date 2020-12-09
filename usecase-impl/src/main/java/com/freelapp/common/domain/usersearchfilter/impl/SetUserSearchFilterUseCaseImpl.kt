package com.freelapp.common.domain.usersearchfilter.impl

import com.freelapp.common.domain.usersearchfilter.SetUserSearchFilterUseCase
import com.freelapp.common.entity.item.Item
import com.freelapp.common.entity.User
import com.freelapp.common.repository.user.UserRepository
import javax.inject.Inject

class SetUserSearchFilterUseCaseImpl<UserType, DataType> @Inject constructor(
    private val userRepository: UserRepository<UserType, DataType>
) : SetUserSearchFilterUseCase where UserType : User<UserType, DataType>,
                                     DataType : Item {

    override fun invoke(query: String) {
        userRepository.setSearchFilter(query)
    }
}