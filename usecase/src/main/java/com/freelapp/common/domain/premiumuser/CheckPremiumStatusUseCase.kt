package com.freelapp.common.domain.premiumuser

import kotlinx.coroutines.flow.StateFlow

fun interface CheckPremiumStatusUseCase {
    operator fun invoke(): StateFlow<Boolean>
}