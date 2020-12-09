package com.freelapp.common.domain.usersearchfilter.impl

import com.freelapp.common.domain.usersearchfilter.SetUserSearchFilterUseCase
import com.freelapp.common.entity.DataOwner
import com.freelapp.common.entity.Data
import com.freelapp.common.repository.user.UserRepository

class SetUserSearchFilterUseCaseImpl<Owner, DataType>(
    private val userRepository: UserRepository<Owner, DataType>
) : SetUserSearchFilterUseCase where Owner : DataOwner<Owner, DataType>,
                                     DataType : Data<DataType> {

    override fun invoke(query: String) {
        userRepository.setSearchFilter(query)
    }
}