package com.freelapp.common.domain.getdatatimelines.impl

import com.freelapp.common.domain.getallitems.GetAllItemsUseCase
import com.freelapp.common.domain.getcurrentuser.GetCurrentUserUseCase
import com.freelapp.common.domain.getdatatimelines.GetDataTimelinesUseCase
import com.freelapp.common.domain.getdatatimelines.impl.ktx.filterByQueryText
import com.freelapp.common.domain.getdatatimelines.impl.ktx.filterInteractedWithBetween
import com.freelapp.common.domain.getdatatimelines.impl.ktx.hideShowOwn
import com.freelapp.common.domain.getdatatimelines.impl.ktx.sortedByPopularity
import com.freelapp.common.domain.hideshowowndata.GetHideShowOwnDataUseCase
import com.freelapp.common.domain.usersearchfilter.GetUserSearchFilterUseCase
import com.freelapp.common.entity.Item
import com.freelapp.common.entity.Timeline
import com.freelapp.common.entity.User
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

    private fun Timeline.items(): StateFlow<List<DataType>> =
        getAllItemsUseCase()
            .filterByQueryText(getUserSearchFilterUseCase)
            .hideShowOwn(getCurrentUserUseCase, getHideShowOwnDataUseCase)
            .filterInteractedWithBetween(now, this)
            .sortedByPopularity()
            .flowOn(Dispatchers.Default)
            .stateIn(
                scope,
                SharingStarted.WhileSubscribed(5000L),
                emptyList()
            )

    private val timelines: Map<Timeline, StateFlow<List<DataType>>> = mapOf(
        Timeline.DAY to Timeline.DAY.items(),
        Timeline.WEEK to Timeline.WEEK.items(),
        Timeline.MONTH to Timeline.MONTH.items(),
    )

    override fun invoke(): Map<Timeline, StateFlow<List<DataType>>> = timelines
}