package com.freelapp.common.domain.hideshowowndata.impl

import com.freelapp.common.domain.hideshowowndata.SetHideShowOwnDataUseCase
import com.freelapp.common.entity.Item
import com.freelapp.common.entity.User
import com.freelapp.common.repository.user.UserRepository
import javax.inject.Inject

class SetHideShowOwnDataUseCaseImpl<UserType, DataType> @Inject constructor(
    private val userRepository: UserRepository<UserType, DataType>
) : SetHideShowOwnDataUseCase where UserType : User<UserType, DataType>,
                                    DataType : Item<DataType> {

    override fun invoke(show: Boolean) {
        userRepository.setHideShowOwnData(show)
    }
}