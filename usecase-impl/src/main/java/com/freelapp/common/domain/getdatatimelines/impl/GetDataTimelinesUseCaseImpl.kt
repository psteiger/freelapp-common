package com.freelapp.common.domain.getdatatimelines.impl

import com.freelapp.common.domain.getallitems.GetAllItemsUseCase
import com.freelapp.common.domain.getallusers.GetAllUsersUseCase
import com.freelapp.common.domain.getcurrentuser.GetCurrentUserUseCase
import com.freelapp.common.domain.getdatatimelines.GetDataTimelinesUseCase
import com.freelapp.common.domain.hideshowowndata.GetHideShowOwnDataUseCase
import com.freelapp.common.domain.usersearchfilter.GetUserSearchFilterUseCase
import com.freelapp.common.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.datetime.Clock

@ExperimentalCoroutinesApi
class GetDataTimelinesUseCaseImpl<UserType, DataType>(
    private val scope: CoroutineScope,
    private val getAllItemsUseCase: GetAllItemsUseCase<DataType>,
    private val getCurrentUserUseCase: GetCurrentUserUseCase<UserType, DataType>,
    private val getHideShowOwnDataUseCase: GetHideShowOwnDataUseCase,
    private val getUserSearchFilterUseCase: GetUserSearchFilterUseCase,
) : GetDataTimelinesUseCase<DataType> where UserType : User<UserType, DataType>,
                                            DataType : Item<DataType> {

    private val now = Clock.System.now().toEpochMilliseconds()

    private fun Flow<List<DataType>>.filtered(): Flow<List<DataType>> =
        combine(getUserSearchFilterUseCase()) { items, text ->
            if (text.isBlank()) items else items.filter { it.matchesQuery(text) }
        }.flowOn(Dispatchers.Default)

    private fun Flow<List<DataType>>.hideShowOwn(): Flow<List<DataType>> =
        combine(
            this,
            getCurrentUserUseCase(),
            getHideShowOwnDataUseCase()
        ) { items, currentUser, show ->
            if (!show && currentUser != null && currentUser.data.isNotEmpty()) {
                val currentUserDataIds = currentUser.data.map { it.id }
                items.filter { it.id !in currentUserDataIds }
            } else items
        }.flowOn(Dispatchers.Default)

    private fun createTimeline(timeline: Timeline): StateFlow<List<DataType>> =
        getAllItemsUseCase()
            .filtered()
            .hideShowOwn()
            .mapLatest { latestItems ->
                latestItems
                    .filter { it.playedAfter(timeline) }
                    .groupingBy { it }
                    .eachCount()
                    .mapKeys { (data, freq) -> data.clone(freq) }
                    .keys
                    .sortedWith(compareBy({ it.freq }, { it.name }))
                    .reversed()
            }
            .flowOn(Dispatchers.Default)
            .stateIn(
                scope,
                SharingStarted.WhileSubscribed(5000L),
                emptyList()
            )

    private val timelines: Map<Timeline, StateFlow<List<DataType>>> = mapOf(
        Timeline.DAY to createTimeline(Timeline.DAY),
        Timeline.WEEK to createTimeline(Timeline.WEEK),
        Timeline.MONTH to createTimeline(Timeline.MONTH),
    )

    override fun invoke(): Map<Timeline, StateFlow<List<DataType>>> = timelines

    private fun DataType?.playedAfter(timeAgo: Timeline): Boolean =
        this?.timestamp?.playedAfter(timeAgo) ?: false

    private fun Long.playedAfter(timeAgo: Timeline): Boolean =
        now - this < timeAgo.toMs()
}