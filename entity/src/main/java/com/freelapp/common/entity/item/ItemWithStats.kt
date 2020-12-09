package com.freelapp.common.entity.item

import com.freelapp.common.entity.Key

interface ItemWithStats : ItemBase, TimestampsOwner {
    interface Factory<T : ItemWithStats> {
        fun create(
            id: Key,
            name: String,
            timestamps: List<Long>
        ): T
    }
    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
}
