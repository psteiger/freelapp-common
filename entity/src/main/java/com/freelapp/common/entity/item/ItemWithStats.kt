package com.freelapp.common.entity.item

interface ItemWithStats : ItemBase, TimestampsOwner {
    interface Factory<T : ItemBase, U : ItemBase> {
        fun create(
            item: T,
            timestamps: List<Long>
        ): U
    }
    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
}
