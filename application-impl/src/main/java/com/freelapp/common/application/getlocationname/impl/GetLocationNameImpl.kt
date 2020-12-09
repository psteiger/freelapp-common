package com.freelapp.common.application.getlocationname.impl

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.freelapp.common.application.R
import com.freelapp.common.application.entity.Latitude
import com.freelapp.common.application.entity.Longitude
import com.freelapp.common.application.getlocationname.GetLocationName
import com.google.android.libraries.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*

class GetLocationNameImpl(
    private val context: Context,
) : GetLocationName {

    override suspend fun invoke(
        location: Pair<Latitude, Longitude>,
        radius: Int
    ): String? = location.getName(radius)

    private suspend fun Pair<Latitude, Longitude>.getName(radius: Int) =
        LatLng(first, second).getName(radius)

    private suspend fun LatLng.getName(radius: Int) =
        getName(context)?.let { name ->
            "$name (${radius.toLocalizedString(context)})"
        }

    private suspend fun LatLng.getName(context: Context): String? =
        withContext(Dispatchers.IO) {
            val addresses = getAddresses(context)
            if (addresses?.size ?: 0 > 0) {
                addresses!![0].locality
            } else null
        }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun LatLng.getAddresses(context: Context): List<Address>? =
        withContext(Dispatchers.IO) {
            try {
                Geocoder(context, Locale.getDefault())
                    .getFromLocation(latitude, longitude, 1)
            } catch (_: IOException) {
                null
            }
        }

    private fun Int.toLocalizedString(context: Context) =
        if (Locale.getDefault().country == "US") {
            val miles = context.getString(R.string.miles)
            "${(this * 0.6213712).toInt()} $miles"
        } else {
            val km = context.getString(R.string.km)
            "$this $km"
        }

}