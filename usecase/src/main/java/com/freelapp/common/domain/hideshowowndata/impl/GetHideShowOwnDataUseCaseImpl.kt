package com.freelapp.common.domain.hideshowowndata.impl

import com.freelapp.common.domain.hideshowowndata.GetHideShowOwnDataUseCase
import com.freelapp.common.entity.DataOwner
import com.freelapp.common.entity.Data
import com.freelapp.common.repository.user.UserRepository
import kotlinx.coroutines.flow.StateFlow

class GetHideShowOwnDataUseCaseImpl<Owner, DataType>(
    private val userRepository: UserRepository<Owner, DataType>
) : GetHideShowOwnDataUseCase where Owner : DataOwner<Owner, DataType>,
                                    DataType : Data<DataType> {

    override fun invoke(): StateFlow<Boolean> = userRepository.hideShowOwnData
}