package com.freelapp.common.domain.usersearchradius

fun interface SetUserSearchRadiusUseCase {
    operator fun invoke(radius: Int)
}