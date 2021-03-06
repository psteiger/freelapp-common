package com.freelapp.common.domain.usersearchradius.impl

import com.freelapp.common.domain.usersearchradius.SetUserSearchRadiusUseCase
import com.freelapp.common.entity.item.Item
import com.freelapp.common.entity.User
import com.freelapp.common.repository.user.UserRepository
import javax.inject.Inject

class SetUserSearchRadiusUseCaseImpl<UserType, DataType> @Inject constructor(
    private val userRepository: UserRepository<UserType, DataType>
) : SetUserSearchRadiusUseCase where UserType : User<DataType>,
                                     DataType : Item {

    override fun invoke(radius: Int) {
        userRepository.setSearchRadius(radius)
    }
}