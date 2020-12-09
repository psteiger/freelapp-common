package com.freelapp.common.domain.getdatatimelines.impl

import com.freelapp.common.domain.getdatatimelines.GetDataTimelines
import com.freelapp.common.domain.hideshowowndata.GetHideShowOwnDataUseCase
import com.freelapp.common.domain.usersearchfilter.GetUserSearchFilterUseCase
import com.freelapp.common.domain.usersearchmode.GetUserSearchModeUseCase
import com.freelapp.common.entity.*
import com.freelapp.common.repository.currentuser.CurrentUserRepository
import com.freelapp.common.repository.user.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.datetime.Clock

@ExperimentalCoroutinesApi
class GetDataTimelinesImpl<Owner, DataType>(
    private val scope: CoroutineScope,
    private val userRepository: UserRepository<Owner, DataType>,
    private val currentUserRepository: CurrentUserRepository<Owner, DataType>,
    private val getHideShowOwnDataUseCase: GetHideShowOwnDataUseCase,
    private val getUserSearchFilterUseCase: GetUserSearchFilterUseCase,
    getUserSearchModeUseCase: GetUserSearchModeUseCase
) : GetDataTimelines<DataType> where Owner : DataOwner<Owner, DataType>,
                                     DataType : Data<DataType> {

    override fun invoke(): Map<Timeline, SharedFlow<List<DataType>>> = timelines

    private val now = Clock.System.now().toEpochMilliseconds()

    private val timelines = mapOf(
        Timeline.DAY to createTimeline(Timeline.DAY),
        Timeline.WEEK to createTimeline(Timeline.WEEK),
        Timeline.MONTH to createTimeline(Timeline.MONTH),
    )

    private val users: Flow<Map<Key, Owner>> =
        getUserSearchModeUseCase()
            .flatMapLatest {
                when (it) {
                    Mode.NEARBY -> userRepository.nearbyUsers
                    Mode.WORLD -> userRepository.globalUsers
                }
            }
            .filtered()
            .hideShowOwnData()
            .flowOn(Dispatchers.Default)

    private val items: Flow<List<DataType>> =
        users
            .mapLatest { userMap ->
                userMap
                    .values
                    .flatMap { it.data }
            }
            .flowOn(Dispatchers.Default)

    private fun Flow<Map<Key, Owner>>.filtered(): Flow<Map<Key, Owner>> =
        combine(getUserSearchFilterUseCase()) { users, text ->
            if (text.isNotBlank()) {
                users.mapValues { (_, owner) ->
                    val data = owner.data
                    val newData = data.filter { it.matchesQuery(text) }
                    if (data.size != newData.size) owner.clone(newData) else owner
                }
            } else users
        }.flowOn(Dispatchers.Default)

    private fun Flow<Map<Key, Owner>>.hideShowOwnData(): Flow<Map<Key, Owner>> =
        combine(
            this,
            currentUserRepository.user,
            getHideShowOwnDataUseCase()
        ) { users, currentUser, show ->
            if (!show && currentUser != null && currentUser.data.isNotEmpty()) {
                users.mapValues { (_, owner) ->
                    val currentUserData = currentUser.data
                    val data = owner.data
                    owner.clone(data - currentUserData.toHashSet())
                }
            } else users
        }.flowOn(Dispatchers.Default)

    private fun createTimeline(timeline: Timeline): SharedFlow<List<DataType>> =
        items
            .mapLatest { userMap ->
                userMap
                    .filter { it.playedAfter(timeline) }
                    .groupingBy { it }
                    .eachCount()
                    .mapKeys { (data, freq) -> data.clone(freq) }
                    .keys
                    .sortedWith(compareBy({ it.freq }, { it.name }))
                    .reversed()
            }
            .flowOn(Dispatchers.Default)
            .shareIn(
                scope,
                SharingStarted.WhileSubscribed(5000L),
                0
            )

    private fun DataType?.playedAfter(timeAgo: Timeline): Boolean =
        this?.timestamp?.playedAfter(timeAgo) ?: false

    private fun Long.playedAfter(timeAgo: Timeline): Boolean =
        now - this < timeAgo.toMs()
}