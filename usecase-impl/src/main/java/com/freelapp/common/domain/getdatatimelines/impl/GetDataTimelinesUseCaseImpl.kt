package com.freelapp.common.domain.getdatatimelines.impl

import com.freelapp.common.domain.getdatatimelines.GetDataTimelinesUseCase
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
class GetDataTimelinesUseCaseImpl<UserType, DataType>(
    private val scope: CoroutineScope,
    private val userRepository: UserRepository<UserType, DataType>,
    private val currentUserRepository: CurrentUserRepository<UserType, DataType>,
    private val getHideShowOwnDataUseCase: GetHideShowOwnDataUseCase,
    private val getUserSearchFilterUseCase: GetUserSearchFilterUseCase,
    getUserSearchModeUseCase: GetUserSearchModeUseCase
) : GetDataTimelinesUseCase<DataType> where UserType : User<UserType, DataType>,
                                            DataType : Item<DataType> {

    private val now = Clock.System.now().toEpochMilliseconds()

    private fun Flow<Map<Key, UserType>>.filtered(): Flow<Map<Key, UserType>> =
        combine(getUserSearchFilterUseCase()) { users, text ->
            if (text.isNotBlank()) {
                users.mapValues { (_, user) ->
                    val data = user.data
                    val newData = data.filter { it.matchesQuery(text) }
                    if (data.size != newData.size) user.clone(newData) else user
                }
            } else users
        }.flowOn(Dispatchers.Default)

    private fun Flow<Map<Key, UserType>>.hideShowOwnData(): Flow<Map<Key, UserType>> =
        combine(
            this,
            currentUserRepository.user,
            getHideShowOwnDataUseCase()
        ) { users, currentUser, show ->
            if (!show && currentUser != null && currentUser.data.isNotEmpty()) {
                users.mapValues { (_, user) ->
                    val currentUserDataIds = currentUser.data.map { it.id }
                    val newData = user.data.filter { it.id !in currentUserDataIds }
                    user.clone(newData)
                }
            } else users
        }.flowOn(Dispatchers.Default)

    private val users: Flow<Map<Key, UserType>> =
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

    private fun createTimeline(timeline: Timeline): SharedFlow<List<DataType>> =
        items
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
            .shareIn(
                scope,
                SharingStarted.WhileSubscribed(5000L),
                1
            )

    private val timelines = mapOf(
        Timeline.DAY to createTimeline(Timeline.DAY),
        Timeline.WEEK to createTimeline(Timeline.WEEK),
        Timeline.MONTH to createTimeline(Timeline.MONTH),
    )

    override fun invoke(): Map<Timeline, SharedFlow<List<DataType>>> = timelines

    private fun DataType?.playedAfter(timeAgo: Timeline): Boolean =
        this?.timestamp?.playedAfter(timeAgo) ?: false

    private fun Long.playedAfter(timeAgo: Timeline): Boolean =
        now - this < timeAgo.toMs()
}