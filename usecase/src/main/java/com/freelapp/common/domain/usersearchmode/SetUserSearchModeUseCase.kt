package com.freelapp.common.domain.usersearchmode

import com.freelapp.common.entity.SearchMode

fun interface SetUserSearchModeUseCase {
    operator fun invoke(searchMode: SearchMode)
}