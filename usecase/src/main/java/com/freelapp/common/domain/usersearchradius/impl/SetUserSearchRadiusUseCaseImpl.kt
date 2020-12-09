package com.freelapp.common.domain.usersearchradius.impl

import com.freelapp.common.domain.usersearchradius.SetUserSearchRadiusUseCase
import com.freelapp.common.entity.DataOwner
import com.freelapp.common.entity.Data
import com.freelapp.common.repository.user.UserRepository

class SetUserSearchRadiusUseCaseImpl<Owner, DataType>(
    private val userRepository: UserRepository<Owner, DataType>
) : SetUserSearchRadiusUseCase where Owner : DataOwner<Owner, DataType>,
                                     DataType : Data<DataType> {

    override fun invoke(radius: Int) {
        userRepository.setSearchRadius(radius)
    }
}