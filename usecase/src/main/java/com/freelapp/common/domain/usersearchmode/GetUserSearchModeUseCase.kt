package com.freelapp.common.domain.usersearchmode

import com.freelapp.common.entity.Mode
import kotlinx.coroutines.flow.StateFlow

interface GetUserSearchModeUseCase {
    operator fun invoke(): StateFlow<Mode>
}