package com.freelapp.common.domain.getdatatimelines.impl.ktx

import com.freelapp.common.domain.getcurrentuser.GetCurrentUserUseCase
import com.freelapp.common.domain.hideshowowndata.GetHideShowOwnDataUseCase
import com.freelapp.common.domain.usersearchfilter.GetUserSearchFilterUseCase
import com.freelapp.common.entity.Item
import com.freelapp.common.entity.Timeline
import com.freelapp.common.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest

internal fun <T : Item<T>> Flow<List<T>>.filterByQueryText(
    getUserSearchFilterUseCase: GetUserSearchFilterUseCase
): Flow<List<T>> =
    combine(getUserSearchFilterUseCase()) { items, text ->
        if (text.isBlank()) items else items.filter { it.matchesQuery(text) }
    }.flowOn(Dispatchers.Default)

internal fun <U : User<U, T>, T : Item<T>> Flow<List<T>>.hideShowOwn(
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
internal fun <T : Item<T>> Flow<List<T>>.filterInteractedWithBetween(
    now: Long,
    timeline: Timeline
): Flow<List<T>> = mapLatest { it.filterInteractedWithBetween(now, timeline) }

internal fun <T : Item<T>> List<T>.filterInteractedWithBetween(
    now: Long,
    timeline: Timeline
): List<T> = filter { it.filterInteractedWithBetween(now, timeline) }

@ExperimentalCoroutinesApi
internal fun <T : Item<T>> Flow<List<T>>.sortedByPopularity(): Flow<List<T>> =
    mapLatest { latestItems ->
        latestItems
            .groupingBy { it }
            .eachCount()
            .mapKeys { (data, freq) -> data.clone(freq) }
            .keys
            .sortedWith(compareBy({ it.freq }, { it.name }))
            .reversed()
    }

private fun <T : Item<T>> T?.filterInteractedWithBetween(
    now: Long,
    timeAgo: Timeline
): Boolean = this?.timestamp?.filterInteractedWithBetween(now, timeAgo) ?: false

private fun Long.filterInteractedWithBetween(
    now: Long,
    timeAgo: Timeline
): Boolean = now - this < timeAgo.toMs()