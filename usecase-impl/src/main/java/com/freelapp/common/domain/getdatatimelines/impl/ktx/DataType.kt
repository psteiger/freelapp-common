package com.freelapp.common.domain.getdatatimelines.impl.ktx

import com.freelapp.common.domain.getcurrentuser.GetCurrentUserUseCase
import com.freelapp.common.domain.hideshowowndata.GetHideShowOwnDataUseCase
import com.freelapp.common.domain.usersearchfilter.GetUserSearchFilterUseCase
import com.freelapp.common.entity.*
import com.freelapp.common.entity.item.Item
import com.freelapp.common.entity.item.ItemBase
import com.freelapp.common.entity.item.ItemWithStats
import com.freelapp.common.entity.item.TimestampsOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest

internal fun <T : ItemBase> Flow<List<T>>.filterByQueryText(
    getUserSearchFilterUseCase: GetUserSearchFilterUseCase
): Flow<List<T>> =
    combine(getUserSearchFilterUseCase()) { items, text ->
        if (text.isBlank()) items else items.filter { it.matchesQuery(text) }
    }.flowOn(Dispatchers.Default)

internal fun <U : User<U, T>, T : Item> Flow<List<T>>.hideShowOwn(
    getCurrentUserUseCase: GetCurrentUserUseCase<U, T>,
    getHideShowOwnDataUseCase: GetHideShowOwnDataUseCase
): Flow<List<T>> =
    combine(this, getCurrentUserUseCase(), getHideShowOwnDataUseCase()) { items, user, show ->
        if (!show && user != null && user.data.isNotEmpty()) {
            val currentUserDataIds = user.data.map { it.id }
            items.filter { it.id !in currentUserDataIds }
        } else items
    }.flowOn(Dispatchers.Default)

@ExperimentalCoroutinesApi
internal fun <T : TimestampsOwner> Flow<List<T>>.filterInteractedWithBetween(
    now: Long,
    timeline: Timeline
): Flow<List<T>> =
    mapLatest { items ->
        items.filter { it.freq(now, timeline) > 0 }
    }

//@ExperimentalCoroutinesApi
//internal fun <T : TimestampOwner> Flow<List<T>>.filterInteractedWithBetween(
//    now: Long,
//    timeline: Timeline
//): Flow<List<T>> = mapLatest { it.filterInteractedWithBetween(now, timeline) }

//internal fun <T : TimestampOwner> List<T>.filterInteractedWithBetween(
//    now: Long,
//    timeline: Timeline
//): List<T> = filter { it.interactedWithBetween(now, timeline) }

@ExperimentalCoroutinesApi
internal fun <T : Item, U : ItemWithStats> Flow<List<T>>.groupEquals(
    itemWithStatsFactory: ItemWithStats.Factory<T, U>
): Flow<List<U>> =
    mapLatest { items ->
        items
            .groupBy({ it }, { it.timestamp })
            .map { (item, timestamps) ->
                itemWithStatsFactory.create(item, timestamps)
            }
    }

@ExperimentalCoroutinesApi
internal fun <T : ItemWithStats> Flow<List<T>>.sortedByPopularity(
    now: Long,
    timeline: Timeline
): Flow<List<T>> =
    mapLatest { items ->
        items
            .sortedWith(compareBy({ it.freq(now, timeline) }, { it.name }))
            .reversed()
    }

//private fun <T : TimestampOwner> T?.interactedWithBetween(
//    now: Long,
//    timeAgo: Timeline
//): Boolean = this?.timestamp?.interactedWithBetween(now, timeAgo) ?: false

private fun Long.interactedWithBetween(
    now: Long,
    timeAgo: Timeline
): Boolean = now - this < timeAgo.toMs()