package com.freelapp.common.application.updatelocation

import kotlinx.coroutines.flow.Flow

interface LifecycleCollector {
    operator fun invoke(vararg flows: Flow<*>)
    operator fun <T> invoke(flow: Flow<T>, fn: suspend (T) -> Unit)
    operator fun <T> invoke(flow: Flow<T>)
}