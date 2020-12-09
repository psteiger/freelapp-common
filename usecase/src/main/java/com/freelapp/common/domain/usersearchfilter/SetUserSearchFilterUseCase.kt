package com.freelapp.common.domain.usersearchfilter

interface SetUserSearchFilterUseCase {
    operator fun invoke(query: String)
}