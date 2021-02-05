package com.freelapp.common.datasource.impl

import android.content.Context
import android.location.Location
import com.freelapp.common.datasource.OptionsDataSource
import com.freelapp.common.datasource.impl.ktx.persist.getValue
import com.freelapp.common.datasource.impl.ktx.persist.persist
import com.freelapp.common.datasource.impl.ktx.persist.setValue
import com.freelapp.common.entity.SearchMode
import com.freelapp.libs.locationfetcher.LocationSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

open class OptionsDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val locationSource: LocationSource,
) : OptionsDataSource {

    private val _hideShowOwnDataFlow = MutableStateFlow(true)
    private var _hideShowOwnData: Boolean by context.persist(_hideShowOwnDataFlow)
    override val hideShowOwnData = _hideShowOwnDataFlow.asStateFlow()
    override fun setHideShowOwnData(show: Boolean) {
        _hideShowOwnData = show
    }

    private val _searchRadiusFlow = MutableStateFlow(300)
    private var _searchRadius: Int by context.persist(_searchRadiusFlow)
    final override val searchRadius = _searchRadiusFlow.asStateFlow()
    override fun setSearchRadius(radius: Int) {
        _searchRadius = radius
    }

    private val _searchText = MutableStateFlow("")
    override val searchText = _searchText.asStateFlow()
    override fun setSearchFilter(query: String) {
        _searchText.value = query
    }

    private val _searchMode: MutableStateFlow<SearchMode> = MutableStateFlow(SearchMode.Nearby.Real)
    override val searchMode = _searchMode.asStateFlow()
    override fun setSearchMode(searchMode: SearchMode) {
        _searchMode.value = searchMode
        if (searchMode is SearchMode.Nearby.Custom) {
            val location = Location("CustomLocation").apply {
                latitude = searchMode.location.first
                longitude = searchMode.location.second
            }
            locationSource.setCustomLocation(location)
            locationSource.setPreferredSource(LocationSource.Source.CUSTOM)
        } // TODO way to change back to real location
    }
}