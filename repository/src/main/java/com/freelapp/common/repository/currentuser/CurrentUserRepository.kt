package com.freelapp.common.repository.currentuser

import com.freelapp.common.entity.DataOwner
import com.freelapp.common.entity.Data
import kotlinx.coroutines.flow.StateFlow

interface CurrentUserRepository<Owner, DataType> where Owner : DataOwner<Owner, DataType>,
                                                       DataType : Data<DataType> {
    val user: StateFlow<Owner?>
}