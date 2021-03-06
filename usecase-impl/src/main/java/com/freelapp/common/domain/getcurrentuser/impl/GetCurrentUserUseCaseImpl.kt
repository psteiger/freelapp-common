package com.freelapp.common.domain.getcurrentuser.impl

import com.freelapp.common.domain.getcurrentuser.GetCurrentUserUseCase
import com.freelapp.common.entity.item.Item
import com.freelapp.common.entity.User
import com.freelapp.common.repository.currentuser.CurrentUserRepository
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class GetCurrentUserUseCaseImpl<UserType, DataType> @Inject constructor(
    private val currentUserRepository: CurrentUserRepository<UserType, DataType>
) : GetCurrentUserUseCase<UserType, DataType> where UserType : User<DataType>,
                                                    DataType : Item {

    override fun invoke(): SharedFlow<UserType?> = currentUserRepository.user
}