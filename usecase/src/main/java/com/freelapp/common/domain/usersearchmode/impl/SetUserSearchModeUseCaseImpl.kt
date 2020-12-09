package com.freelapp.common.domain.usersearchmode.impl

import com.freelapp.common.domain.usersearchmode.SetUserSearchModeUseCase
import com.freelapp.common.entity.DataOwner
import com.freelapp.common.entity.Data
import com.freelapp.common.entity.Mode
import com.freelapp.common.repository.user.UserRepository

class SetUserSearchModeUseCaseImpl<Owner, DataType>(
    private val userRepository: UserRepository<Owner, DataType>
) : SetUserSearchModeUseCase where Owner : DataOwner<Owner, DataType>,
                                   DataType : Data<DataType> {

    override fun invoke(mode: Mode) {
        userRepository.setSearchMode(mode)
    }
}