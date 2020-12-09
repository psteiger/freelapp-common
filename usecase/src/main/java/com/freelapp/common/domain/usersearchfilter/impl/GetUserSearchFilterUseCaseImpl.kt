package com.freelapp.common.domain.usersearchfilter.impl

import com.freelapp.common.domain.usersearchfilter.GetUserSearchFilterUseCase
import com.freelapp.common.entity.DataOwner
import com.freelapp.common.entity.Data
import com.freelapp.common.repository.user.UserRepository
import kotlinx.coroutines.flow.StateFlow

class GetUserSearchFilterUseCaseImpl<Owner, DataType>(
    private val userRepository: UserRepository<Owner, DataType>
) : GetUserSearchFilterUseCase where Owner : DataOwner<Owner, DataType>,
                                     DataType : Data<DataType> {

    override fun invoke(): StateFlow<String> = userRepository.searchText
}