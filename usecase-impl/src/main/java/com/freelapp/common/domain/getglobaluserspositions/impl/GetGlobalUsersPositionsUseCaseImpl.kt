package com.freelapp.common.domain.getglobaluserspositions.impl

import com.freelapp.common.domain.getglobaluserspositions.GetGlobalUsersPositionsUseCase
import com.freelapp.common.entity.*
import com.freelapp.common.repository.user.UserRepository
import kotlinx.coroutines.flow.StateFlow

class GetGlobalUsersPositionsUseCaseImpl<UserType, DataType>(
    private val userRepository: UserRepository<UserType, DataType>
) : GetGlobalUsersPositionsUseCase where UserType : User<UserType, DataType>,
                                         DataType : Item<DataType> {

    override fun invoke(): StateFlow<Map<Key, Pair<Latitude, Longitude>>> =
        userRepository.globalUsersPositions
}