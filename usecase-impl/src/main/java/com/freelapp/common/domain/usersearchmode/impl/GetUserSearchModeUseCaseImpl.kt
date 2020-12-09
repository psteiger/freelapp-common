package com.freelapp.common.domain.usersearchmode.impl

import com.freelapp.common.domain.usersearchmode.GetUserSearchModeUseCase
import com.freelapp.common.entity.item.Item
import com.freelapp.common.entity.SearchMode
import com.freelapp.common.entity.User
import com.freelapp.common.repository.user.UserRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetUserSearchModeUseCaseImpl<UserType, DataType> @Inject constructor(
    private val userRepository: UserRepository<UserType, DataType>
) : GetUserSearchModeUseCase where UserType : User<UserType, DataType>,
                                   DataType : Item {

    override fun invoke(): StateFlow<SearchMode> = userRepository.searchMode
}