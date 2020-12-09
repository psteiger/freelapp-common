package com.freelapp.common.entity

sealed class SearchMode {
    sealed class Nearby : SearchMode() {
        object Real : Nearby()
        data class Custom(val location: Pair<Latitude, Longitude>) : Nearby()
    }

    object Worldwide : SearchMode()
}