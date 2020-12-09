package com.freelapp.common.domain.usersearchfilter

import kotlinx.coroutines.flow.StateFlow

interface GetUserSearchFilterUseCase {
    operator fun invoke(): StateFlow<String>
}