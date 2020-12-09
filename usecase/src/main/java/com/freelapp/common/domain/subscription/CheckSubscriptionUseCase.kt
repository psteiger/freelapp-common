package com.freelapp.common.domain.subscription

import kotlinx.coroutines.flow.StateFlow

interface CheckSubscriptionUseCase {
    operator fun invoke(): StateFlow<Boolean>
}