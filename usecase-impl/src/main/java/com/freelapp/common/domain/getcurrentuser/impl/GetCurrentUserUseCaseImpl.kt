package com.freelapp.common.domain.getcurrentuser.impl

import com.freelapp.common.domain.getcurrentuser.GetCurrentUserUseCase
import com.freelapp.common.entity.Item
import com.freelapp.common.entity.User
import com.freelapp.common.repository.currentuser.CurrentUserRepository
import kotlinx.coroutines.flow.StateFlow

class GetCurrentUserUseCaseImpl<UserType, DataType>(
    private val currentUserRepository: CurrentUserRepository<UserType, DataType>
) : GetCurrentUserUseCase<UserType, DataType> where UserType : User<UserType, DataType>,
                                                    DataType : Item<DataType> {

    override fun invoke(): StateFlow<UserType?> = currentUserRepository.user
}