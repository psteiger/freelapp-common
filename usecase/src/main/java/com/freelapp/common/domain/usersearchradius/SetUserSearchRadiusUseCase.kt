package com.freelapp.common.domain.usersearchradius

interface SetUserSearchRadiusUseCase {
    operator fun invoke(radius: Int)
}