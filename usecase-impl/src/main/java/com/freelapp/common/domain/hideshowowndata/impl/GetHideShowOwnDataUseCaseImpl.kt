package com.freelapp.common.domain.hideshowowndata.impl

import com.freelapp.common.domain.hideshowowndata.GetHideShowOwnDataUseCase
import com.freelapp.common.entity.Item
import com.freelapp.common.entity.User
import com.freelapp.common.repository.user.UserRepository
import kotlinx.coroutines.flow.StateFlow

class GetHideShowOwnDataUseCaseImpl<UserType, DataType>(
    private val userRepository: UserRepository<UserType, DataType>
) : GetHideShowOwnDataUseCase where UserType : User<UserType, DataType>,
                                    DataType : Item<DataType> {

    override fun invoke(): StateFlow<Boolean> = userRepository.hideShowOwnData
}