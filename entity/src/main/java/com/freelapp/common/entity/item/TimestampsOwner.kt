package com.freelapp.common.entity.item

import com.freelapp.common.entity.Timeline

interface TimestampsOwner {
    val timestamps: List<Long>
    fun freq(now: Long, timeline: Timeline): Int =
        timestamps.filter { now - it < timeline.toMs() }.count()
}