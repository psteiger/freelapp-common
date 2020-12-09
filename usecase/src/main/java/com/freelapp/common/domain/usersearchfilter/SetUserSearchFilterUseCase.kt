package com.freelapp.common.domain.usersearchfilter

fun interface SetUserSearchFilterUseCase {
    operator fun invoke(query: String)
}