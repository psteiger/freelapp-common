package com.freelapp.common.entity

import com.freelapp.common.entity.item.Item

interface User<UserType, DataType> where UserType : User<UserType, DataType>,
                                         DataType : Item {
    val id: Key
    val data: List<DataType>
    val location: Pair<Latitude, Longitude>
    fun clone(newData: List<DataType>): UserType
    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
}