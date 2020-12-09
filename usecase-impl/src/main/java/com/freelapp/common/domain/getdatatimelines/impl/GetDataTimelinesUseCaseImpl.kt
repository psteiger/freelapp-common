package com.freelapp.common.domain.getdatatimelines.impl

import com.freelapp.common.domain.getallitems.GetAllItemsUseCase
import com.freelapp.common.domain.getcurrentuser.GetCurrentUserUseCase
import com.freelapp.common.domain.getdatatimelines.GetDataTimelinesUseCase
import com.freelapp.common.domain.getdatatimelines.impl.ktx.*
import com.freelapp.common.domain.hideshowowndata.GetHideShowOwnDataUseCase
import com.freelapp.common.domain.usersearchfilter.GetUserSearchFilterUseCase
import com.freelapp.common.entity.item.Item
import com.freelapp.common.entity.item.ItemWithStats
import com.freelapp.common.entity.Timeline
import com.freelapp.common.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.datetime.Clock
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetDataTimelinesUseCaseImpl<UserType, DataType, DataTypeWithStats> @Inject constructor(
    scope: CoroutineScope,
    getAllItemsUseCase: GetAllItemsUseCase<DataType>,
    getCurrentUserUseCase: GetCurrentUserUseCase<UserType, DataType>,
    getHideShowOwnDataUseCase: GetHideShowOwnDataUseCase,
    getUserSearchFilterUseCase: GetUserSearchFilterUseCase,
    itemWithStatsFactory: ItemWithStats.Factory<DataTypeWithStats>
) : GetDataTimelinesUseCase<DataTypeWithStats> where UserType : User<UserType, DataType>,
                                                     DataType : Item,
                                                     DataTypeWithStats : ItemWithStats {

    private val now = Clock.System.now().toEpochMilliseconds()

    private val filteredItems =
        getAllItemsUseCase()
            .hideShowOwn(getCurrentUserUseCase, getHideShowOwnDataUseCase)
            .groupEquals(itemWithStatsFactory)
            .filterByQueryText(getUserSearchFilterUseCase)
            .flowOn(Dispatchers.Default)
            .stateIn(
                scope,
                SharingStarted.WhileSubscribed(replayExpirationMillis = 0),
                emptyList()
            )

    private fun Timeline.items(scope: CoroutineScope): StateFlow<List<DataTypeWithStats>> =
        filteredItems
            .filterInteractedWithBetween(now, this)
            .sortedByPopularity(now, this)
            .flowOn(Dispatchers.Default)
            .stateIn(
                scope,
                SharingStarted.WhileSubscribed(replayExpirationMillis = 0),
                emptyList()
            )

    private val timelines: Map<Timeline, StateFlow<List<DataTypeWithStats>>> = mapOf(
        Timeline.DAY to Timeline.DAY.items(scope),
        Timeline.WEEK to Timeline.WEEK.items(scope),
        Timeline.MONTH to Timeline.MONTH.items(scope),
    )

    override fun invoke(): Map<Timeline, StateFlow<List<DataTypeWithStats>>> = timelines
}