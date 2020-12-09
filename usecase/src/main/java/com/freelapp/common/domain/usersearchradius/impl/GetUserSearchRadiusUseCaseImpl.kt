package com.freelapp.common.domain.usersearchradius.impl

import com.freelapp.common.domain.usersearchradius.GetUserSearchRadiusUseCase
import com.freelapp.common.entity.DataOwner
import com.freelapp.common.entity.Data
import com.freelapp.common.repository.user.UserRepository
import kotlinx.coroutines.flow.StateFlow

class GetUserSearchRadiusUseCaseImpl<Owner, DataType>(
    private val userRepository: UserRepository<Owner, DataType>
) : GetUserSearchRadiusUseCase where Owner : DataOwner<Owner, DataType>,
                                     DataType : Data<DataType> {

    override fun invoke(): StateFlow<Int> = userRepository.searchRadius
}