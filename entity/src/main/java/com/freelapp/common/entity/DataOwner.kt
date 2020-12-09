package com.freelapp.common.entity

interface DataOwner<Owner, DataType> where Owner : DataOwner<Owner, DataType>,
                                           DataType : Data<DataType> {

    val data: List<DataType>
    val location: Pair<Latitude, Longitude>
    fun clone(newData: List<DataType>): Owner
}