package com.freelapp.common.domain.usersearchradius.impl

import com.freelapp.common.domain.usersearchradius.SetUserSearchRadiusUseCase
import com.freelapp.common.entity.User
import com.freelapp.common.entity.Item
import com.freelapp.common.repository.user.UserRepository

class SetUserSearchRadiusUseCaseImpl<UserType, DataType>(
    private val userRepository: UserRepository<UserType, DataType>
) : SetUserSearchRadiusUseCase where UserType : User<UserType, DataType>,
                                     DataType : Item<DataType> {

    override fun invoke(radius: Int) {
        userRepository.setSearchRadius(radius)
    }
}