package com.freelapp.components.repository.impl.ktx.persist

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.reflect.KProperty

interface Persist<T>

class PersistImpl<T>(
    context: Context,
    val default: T,
    val backingFlow: MutableStateFlow<T>? = null
) : Persist<T> {

    val prefs: SharedPreferences

    init {
        val appContext = context.applicationContext ?: throw IllegalStateException()
        prefs = appContext.getSharedPreferences("stats", Context.MODE_PRIVATE)
    }
}

data class LazyPersist<T>(val data: Lazy<Persist<T>>)

fun <T> Context.persist(
    default: T
): LazyPersist<T> = LazyPersist(
    lazy { PersistImpl(this, default) }
)

fun <T> Context.persist(
    backingFlow: MutableStateFlow<T>
): LazyPersist<T> = LazyPersist(
    lazy { PersistImpl(this, backingFlow.value, backingFlow) }
)

inline operator fun <reified T> LazyPersist<T>.getValue(
    thisRef: Any?,
    property: KProperty<*>
): T {
    val propertyName = property.name.trimStart('_')
    val impl = data.value as PersistImpl
    val prefs = impl.prefs
    val default = impl.default
    val backingFlow = impl.backingFlow

    val value = with(prefs) {
        when (T::class) {
            Int::class -> getInt(propertyName, default as Int) as T
            String::class -> getString(propertyName, default as String) as T
            Boolean::class -> getBoolean(propertyName, default as Boolean) as T
            else -> throw Exception("Error, type=${T::class}")
        }
    }

    backingFlow?.value = value
    return value
}

inline operator fun <reified T> LazyPersist<T>.setValue(
    thisRef: Any?,
    property: KProperty<*>,
    value: T
) {
    val propertyName = property.name.trimStart('_')
    val impl = data.value as PersistImpl
    val backingFlow = impl.backingFlow
    val prefs = impl.prefs
    backingFlow?.value = value
    prefs.edit(true) {
        when (T::class) {
            Int::class -> putInt(propertyName, value as Int)
            String::class -> putString(propertyName, value as String)
            Boolean::class -> putBoolean(propertyName, value as Boolean)
            else -> throw Exception("Error, type=${T::class}")
        }
    }
}

