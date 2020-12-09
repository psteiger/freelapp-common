package com.freelapp.common.entity

interface User<UserType, DataType> where UserType : User<UserType, DataType>,
                                         DataType : Item<DataType> {
    val id: Key
    val data: List<DataType>
    val location: Pair<Latitude, Longitude>
    fun clone(newData: List<DataType>): UserType
}