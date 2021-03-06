package com.freelapp.common.domain.getglobalusers.impl

import com.freelapp.common.domain.getglobalusers.GetGlobalUsersUseCase
import com.freelapp.common.entity.item.Item
import com.freelapp.common.entity.Key
import com.freelapp.common.entity.User
import com.freelapp.common.repository.user.UserRepository
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class GetGlobalUsersUseCaseImpl<UserType, DataType> @Inject constructor(
    private val userRepository: UserRepository<UserType, DataType>
) : GetGlobalUsersUseCase<UserType, DataType> where UserType : User<DataType>,
                                                    DataType : Item {

    override fun invoke(): SharedFlow<Map<Key, UserType>> =
        userRepository.globalUsers
}