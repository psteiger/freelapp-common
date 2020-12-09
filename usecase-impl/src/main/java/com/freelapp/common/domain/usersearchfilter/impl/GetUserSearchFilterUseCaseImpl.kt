package com.freelapp.common.domain.usersearchfilter.impl

import com.freelapp.common.domain.usersearchfilter.GetUserSearchFilterUseCase
import com.freelapp.common.entity.Item
import com.freelapp.common.entity.User
import com.freelapp.common.repository.user.UserRepository
import kotlinx.coroutines.flow.StateFlow

class GetUserSearchFilterUseCaseImpl<UserType, DataType>(
    private val userRepository: UserRepository<UserType, DataType>
) : GetUserSearchFilterUseCase where UserType : User<UserType, DataType>,
                                     DataType : Item<DataType> {

    override fun invoke(): StateFlow<String> = userRepository.searchText
}