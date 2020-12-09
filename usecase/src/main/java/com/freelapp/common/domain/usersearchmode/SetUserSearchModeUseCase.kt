package com.freelapp.common.domain.usersearchmode

import com.freelapp.common.entity.Mode

interface SetUserSearchModeUseCase {
    operator fun invoke(mode: Mode)
}