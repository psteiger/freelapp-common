package com.freelapp.common.domain.hideshowowndata

import kotlinx.coroutines.flow.StateFlow

fun interface GetHideShowOwnDataUseCase {
    operator fun invoke(): StateFlow<Boolean>
}