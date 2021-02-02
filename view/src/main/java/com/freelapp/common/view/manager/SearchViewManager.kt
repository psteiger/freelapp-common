package com.freelapp.common.view.manager

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.provider.BaseColumns
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import com.freelapp.common.domain.getallitems.GetAllItemsUseCase
import com.freelapp.common.domain.usersearchfilter.GetUserSearchFilterUseCase
import com.freelapp.common.domain.usersearchfilter.SetUserSearchFilterUseCase
import com.freelapp.common.entity.item.Item
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@ActivityScoped
class SearchViewManager<DataType : Item> @Inject constructor(
    private val getAllItemsUseCase: GetAllItemsUseCase<DataType>,
    private val getUserSearchFilterUseCase: GetUserSearchFilterUseCase,
    private val setUserSearchFilterUseCase: SetUserSearchFilterUseCase,
    private val componentName: ComponentName,
) {

    fun setupSearchView(searchView: SearchView) {
        searchView.apply {
            setSearchableInfo(
                (context.getSystemService(Context.SEARCH_SERVICE) as SearchManager)
                    .getSearchableInfo(componentName)
            )

            suggestionsAdapter =
                SimpleCursorAdapter(
                    context,
                    android.R.layout.simple_list_item_1,
                    MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1)),
                    arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1),
                    intArrayOf(android.R.id.text1),
                    CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
                ).apply {
                    setFilterQueryProvider {
                        val items = runBlocking { getAllItemsUseCase().first().toSet() }
                        val query = it.toString()
                        setUserSearchFilterUseCase(query)
                        populateSuggestions(items, query)
                    }
                }

            imeOptions = EditorInfo.IME_ACTION_SEARCH

            setOnSuggestionListener(object : SearchView.OnSuggestionListener {
                override fun onSuggestionClick(position: Int): Boolean {
                    suggestionsAdapter.cursor?.run {
                        if (moveToPosition(position)) setUserSearchFilterUseCase(getString(1))
                    }
                    setQuery(getUserSearchFilterUseCase().value, false)
                    clearFocus()
                    return true
                }

                override fun onSuggestionSelect(p0: Int) = true
            })
        }
    }

    private fun populateSuggestions(tracks: Set<DataType>, text: String): Cursor =
        MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
            .apply {
                tracks.forEachIndexed { index, track ->
                    if (track.matchesQuery(text)) addRow(arrayOf(index, track.name))
                }
            }
}