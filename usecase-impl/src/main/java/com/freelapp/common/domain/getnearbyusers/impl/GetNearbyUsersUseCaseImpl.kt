package com.freelapp.common.domain.getnearbyusers.impl

import com.freelapp.common.domain.getnearbyusers.GetNearbyUsersUseCase
import com.freelapp.common.entity.Item
import com.freelapp.common.entity.Key
import com.freelapp.common.entity.User
import com.freelapp.common.repository.user.UserRepository
import kotlinx.coroutines.flow.StateFlow

class GetNearbyUsersUseCaseImpl<UserType, DataType>(
    private val userRepository: UserRepository<UserType, DataType>
) : GetNearbyUsersUseCase<UserType, DataType> where UserType : User<UserType, DataType>,
                                                    DataType : Item<DataType> {

    override fun invoke(): StateFlow<Map<Key, UserType>> =
        userRepository.nearbyUsers
}