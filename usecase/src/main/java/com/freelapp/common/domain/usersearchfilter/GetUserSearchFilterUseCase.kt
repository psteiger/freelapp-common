package com.freelapp.common.domain.usersearchfilter

import kotlinx.coroutines.flow.StateFlow

fun interface GetUserSearchFilterUseCase {
    operator fun invoke(): StateFlow<String>
}