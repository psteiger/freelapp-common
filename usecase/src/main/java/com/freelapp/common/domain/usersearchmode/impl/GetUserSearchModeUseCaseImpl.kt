package com.freelapp.common.domain.usersearchmode.impl

import com.freelapp.common.domain.usersearchmode.GetUserSearchModeUseCase
import com.freelapp.common.entity.DataOwner
import com.freelapp.common.entity.Data
import com.freelapp.common.entity.Mode
import com.freelapp.common.repository.user.UserRepository
import kotlinx.coroutines.flow.StateFlow

class GetUserSearchModeUseCaseImpl<Owner, DataType>(
    private val userRepository: UserRepository<Owner, DataType>
) : GetUserSearchModeUseCase where Owner : DataOwner<Owner, DataType>,
                                   DataType : Data<DataType> {

    override fun invoke(): StateFlow<Mode> = userRepository.searchMode
}