package com.freelapp.common.datasource

import com.freelapp.common.entity.SearchMode
import kotlinx.coroutines.flow.StateFlow

interface OptionsDataSource {
    val searchRadius: StateFlow<Int>
    val searchMode: StateFlow<SearchMode>
    val searchText: StateFlow<String>
    val hideShowOwnData: StateFlow<Boolean>

    fun setSearchRadius(radius: Int)
    fun setSearchMode(searchMode: SearchMode)
    fun setSearchFilter(query: String)
    fun setHideShowOwnData(show: Boolean)
}