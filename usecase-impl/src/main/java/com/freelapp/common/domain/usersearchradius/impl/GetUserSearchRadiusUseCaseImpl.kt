package com.freelapp.common.domain.usersearchradius.impl

import com.freelapp.common.domain.usersearchradius.GetUserSearchRadiusUseCase
import com.freelapp.common.entity.item.Item
import com.freelapp.common.entity.User
import com.freelapp.common.repository.user.UserRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetUserSearchRadiusUseCaseImpl<UserType, DataType> @Inject constructor(
    private val userRepository: UserRepository<UserType, DataType>
) : GetUserSearchRadiusUseCase where UserType : User<UserType, DataType>,
                                     DataType : Item {

    override fun invoke(): StateFlow<Int> = userRepository.searchRadius
}