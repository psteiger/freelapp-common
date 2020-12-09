package com.freelapp.common.entity

interface Data<T> where T : Data<T> {
    val id: Key
    val name: String
    val freq: Int
    val timestamp: Long
    fun matchesQuery(text: String): Boolean
    fun clone(newFreq: Int): T
}