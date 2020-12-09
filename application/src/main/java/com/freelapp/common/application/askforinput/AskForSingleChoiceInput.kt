package com.freelapp.common.application.askforinput

import javax.inject.Provider

interface AskForSingleChoiceInput<T> {
    suspend operator fun invoke(
        title: String,
        message: String,
        items: Map<T, Provider<String>>
    ): T?
}