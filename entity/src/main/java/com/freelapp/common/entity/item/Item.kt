package com.freelapp.common.entity.item

interface Item : ItemBase, TimestampOwner {
    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
}