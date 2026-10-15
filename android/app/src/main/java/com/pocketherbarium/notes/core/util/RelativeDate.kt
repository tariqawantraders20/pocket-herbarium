package com.pocketherbarium.notes.core.util

object RelativeDate {

    private const val DAY_MS = 86400000L

    fun daysBetween(from: Long, to: Long): Int {
        if (from <= 0L || to <= from) return 0
        return ((to - from) / DAY_MS).toInt()
    }

    fun isToday(from: Long, to: Long): Boolean = daysBetween(from, to) < 1
}
