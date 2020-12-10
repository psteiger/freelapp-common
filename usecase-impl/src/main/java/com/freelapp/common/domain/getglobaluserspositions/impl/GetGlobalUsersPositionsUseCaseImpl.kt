package com.freelapp.common.domain.getglobaluserspositions.impl

import com.freelapp.common.domain.getglobaluserspositions.GetGlobalUsersPositionsUseCase
import com.freelapp.common.entity.*
import com.freelapp.common.entity.item.Item
import com.freelapp.common.repository.user.UserRepository
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class GetGlobalUsersPositionsUseCaseImpl<UserType, DataType> @Inject constructor(
    private val userRepository: UserRepository<UserType, DataType>
) : GetGlobalUsersPositionsUseCase where UserType : User<UserType, DataType>,
                                         DataType : Item {

    override fun invoke(): SharedFlow<Map<Key, Pair<Latitude, Longitude>>> =
        userRepository.globalUsersPositions
}