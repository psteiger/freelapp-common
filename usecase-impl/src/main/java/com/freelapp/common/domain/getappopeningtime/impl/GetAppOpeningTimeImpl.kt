package com.freelapp.common.domain.getappopeningtime.impl

import com.freelapp.common.domain.getappopeningtime.GetAppOpeningTime
import kotlinx.datetime.Clock

object GetAppOpeningTimeImpl : GetAppOpeningTime {

    private val appOpeningTime = Clock.System.now().toEpochMilliseconds()

    override fun invoke(): Long = appOpeningTime
}