package com.freelapp.common.entity.item

import com.freelapp.common.entity.Key

interface ItemBase {
    val id: Key
    val name: String
    fun matchesQuery(text: String): Boolean
}