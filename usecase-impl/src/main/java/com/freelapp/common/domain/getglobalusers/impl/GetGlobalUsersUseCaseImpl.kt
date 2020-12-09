package com.freelapp.common.domain.getglobalusers.impl

import com.freelapp.common.domain.getglobalusers.GetGlobalUsersUseCase
import com.freelapp.common.entity.Item
import com.freelapp.common.entity.Key
import com.freelapp.common.entity.User
import com.freelapp.common.repository.user.UserRepository
import kotlinx.coroutines.flow.StateFlow

class GetGlobalUsersUseCaseImpl<UserType, DataType>(
    private val userRepository: UserRepository<UserType, DataType>
) : GetGlobalUsersUseCase<UserType, DataType> where UserType : User<UserType, DataType>,
                                                    DataType : Item<DataType> {

    override fun invoke(): StateFlow<Map<Key, UserType>> =
        userRepository.globalUsers
}