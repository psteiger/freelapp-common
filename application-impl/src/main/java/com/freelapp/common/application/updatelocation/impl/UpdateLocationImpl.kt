package com.freelapp.common.application.updatelocation.impl

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.addRepeatingJob
import com.freelapp.common.application.updatelocation.LifecycleCollector
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class LifecycleCollector @Inject constructor(
    private val owner: LifecycleOwner
) : LifecycleCollector {

    override fun invoke(vararg flows: Flow<*>) {
        flows.forEach { invoke(it) }
    }

    override fun <T> invoke(flow: Flow<T>, fn: suspend (T) -> Unit) {
        owner.addRepeatingJob(Lifecycle.State.STARTED) { flow.collect(fn) }
    }

    override fun <T> invoke(flow: Flow<T>) {
        owner.addRepeatingJob(Lifecycle.State.STARTED) { flow.collect() }
    }
}