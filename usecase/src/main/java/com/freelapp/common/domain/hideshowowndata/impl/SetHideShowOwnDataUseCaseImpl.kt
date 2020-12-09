package com.freelapp.common.domain.hideshowowndata.impl

import com.freelapp.common.domain.hideshowowndata.SetHideShowOwnDataUseCase
import com.freelapp.common.entity.DataOwner
import com.freelapp.common.entity.Data
import com.freelapp.common.repository.user.UserRepository

class SetHideShowOwnDataUseCaseImpl<Owner, DataType>(
    private val userRepository: UserRepository<Owner, DataType>
) : SetHideShowOwnDataUseCase where Owner : DataOwner<Owner, DataType>,
                                    DataType : Data<DataType> {

    override fun invoke(show: Boolean) {
        userRepository.setHideShowOwnData(show)
    }
}