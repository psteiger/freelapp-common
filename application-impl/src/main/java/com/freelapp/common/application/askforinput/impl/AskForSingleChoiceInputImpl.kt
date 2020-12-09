package com.freelapp.common.application.askforinput.impl

import android.app.Activity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.afollestad.materialdialogs.internal.list.DialogAdapter
import com.afollestad.materialdialogs.list.getListAdapter
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.freelapp.common.application.askforinput.AskForSingleChoiceInput
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AskForSingleChoiceInputImpl<T>(
    private val activity: Activity
) : AskForSingleChoiceInput<T> {

    override suspend fun invoke(title: String, message: String, items: Map<T, String>): T? {
        val indexedItems = items.toIndexedMap()
        val selection = MaterialDialog(activity).show {
            title(text = title)
            message(text = message)
            listItemsSingleChoice(items = indexedItems.valuesValues)
        }.awaitSelection() ?: return null
        return indexedItems.getValue(selection).key
    }

    private fun <T, R> Map<T, R>.toIndexedMap(): Map<Int, Map.Entry<T, R>> =
        asSequence()
            .mapIndexed { index, entry -> index to entry }
            .toMap()

    private val <T, R> Map<Int, Map.Entry<T, R>>.valuesValues
        get(): List<R> = map { (_, entry) -> entry.value }

    private suspend fun MaterialDialog.awaitSelection() =
        suspendCancellableCoroutine<Int?> { continuation ->
            var selection: Int? = null
            positiveButton {
                val adapter = getListAdapter() as DialogAdapter<*, *>
                val itemSelected = (0 until adapter.getItemCount()).filter { adapter.isItemChecked(it) }
                require(itemSelected.size == 1)
                selection = itemSelected.first()
            }
            negativeButton { }
            onDismiss { continuation.resume(selection) }
        }
}

/*
                    "${subscriptionSku.price.value}/${activity.getString(R.string.month)}",
                    "${premiumOnceSku.price.value} ${activity.getString(R.string.once)}",
 */