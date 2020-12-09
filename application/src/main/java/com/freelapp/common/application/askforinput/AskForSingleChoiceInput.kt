package com.freelapp.common.application.askforinput

interface AskForSingleChoiceInput<T> {
    suspend operator fun invoke(
        title: String,
        message: String,
        items: Map<T, String>
    ): T?
}