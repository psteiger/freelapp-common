package com.freelapp.common.entity

import com.freelapp.common.entity.item.Item

interface User<DataType : Item> {
    val id: Key
    val data: List<DataType>
    val location: Pair<Latitude, Longitude>
    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
}