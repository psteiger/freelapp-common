package com.freelapp.common.domain.getallusers.impl

import com.freelapp.common.domain.getallusers.GetAllUsersUseCase
import com.freelapp.common.domain.getglobalusers.GetGlobalUsersUseCase
import com.freelapp.common.domain.getnearbyusers.GetNearbyUsersUseCase
import com.freelapp.common.domain.usersearchmode.GetUserSearchModeUseCase
import com.freelapp.common.entity.item.Item
import com.freelapp.common.entity.Key
import com.freelapp.common.entity.SearchMode
import com.freelapp.common.entity.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class GetAllUsersUseCaseImpl<UserType, DataType> @Inject constructor(
    private val getUserSearchModeUseCase: GetUserSearchModeUseCase,
    private val getNearbyUsersUseCase: GetNearbyUsersUseCase<UserType, DataType>,
    private val getGlobalUsersUseCase: GetGlobalUsersUseCase<UserType, DataType>,
) : GetAllUsersUseCase<UserType, DataType> where UserType : User<UserType, DataType>,
                                                 DataType : Item {

    @ExperimentalCoroutinesApi
    override fun invoke(): Flow<Map<Key, UserType>> =
        getUserSearchModeUseCase()
            .flatMapLatest {
                when (it) {
                    SearchMode.Nearby.Real,
                    is SearchMode.Nearby.Custom -> getNearbyUsersUseCase()
                    SearchMode.Worldwide -> getGlobalUsersUseCase()
                }
            }
 }