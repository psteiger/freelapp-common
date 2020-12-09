package com.freelapp.common.entity

enum class Timeline {
    DAY, WEEK, MONTH;

    fun toMs() = when (this) {
        DAY -> DAY_IN_MILLISECONDS
        WEEK -> WEEK_IN_MILLISECONDS
        MONTH -> MONTH_IN_MILLISECONDS
    }

    companion object {
        private const val ONE_HOUR_IN_MILLISECONDS = (60 * 60 * 1000).toLong()
        private const val DAY_IN_MILLISECONDS = ONE_HOUR_IN_MILLISECONDS * 24
        private const val MONTH_IN_MILLISECONDS = DAY_IN_MILLISECONDS * 30
        private const val WEEK_IN_MILLISECONDS = DAY_IN_MILLISECONDS * 7
    }
}