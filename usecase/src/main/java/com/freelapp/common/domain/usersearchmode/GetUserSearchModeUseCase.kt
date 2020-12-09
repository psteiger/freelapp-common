package com.freelapp.common.domain.usersearchmode

import com.freelapp.common.entity.SearchMode
import kotlinx.coroutines.flow.StateFlow

fun interface GetUserSearchModeUseCase {
    operator fun invoke(): StateFlow<SearchMode>
}