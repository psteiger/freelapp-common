package com.freelapp.common.domain.hideshowowndata

fun interface SetHideShowOwnDataUseCase {
    operator fun invoke(show: Boolean)
}