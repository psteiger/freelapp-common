package com.freelapp.common.domain.hideshowowndata.impl

import com.freelapp.common.domain.hideshowowndata.SetHideShowOwnDataUseCase
import com.freelapp.common.entity.item.Item
import com.freelapp.common.entity.User
import com.freelapp.common.repository.user.UserRepository
import javax.inject.Inject

class SetHideShowOwnDataUseCaseImpl<UserType, DataType> @Inject constructor(
    private val userRepository: UserRepository<UserType, DataType>
) : SetHideShowOwnDataUseCase where UserType : User<DataType>,
                                    DataType : Item {

    override fun invoke(show: Boolean) {
        userRepository.setHideShowOwnData(show)
    }
}