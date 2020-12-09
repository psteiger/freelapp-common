package com.freelapp.common.domain.usersearchradius

import kotlinx.coroutines.flow.StateFlow

fun interface GetUserSearchRadiusUseCase {
    operator fun invoke(): StateFlow<Int>
}