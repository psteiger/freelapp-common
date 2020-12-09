package com.freelapp.common.entity

interface Item<T> where T : Item<T> {
    val id: Key
    val name: String
    val freq: Int
    val timestamp: Long
    fun matchesQuery(text: String): Boolean
    fun clone(newFreq: Int): T
    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
}