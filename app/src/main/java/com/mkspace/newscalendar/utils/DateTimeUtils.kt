package com.mkspace.newscalendar.utils

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {

    private const val DEFAULT_DATE_FORMAT_TEXT = "MM월dd일"
    private const val DEFAULT_TIME_ZONE_ID = "GMT+09:00"

    fun getYesterday(formatText: String = DEFAULT_DATE_FORMAT_TEXT): String =
        getFormattedTextForDate(formatText, -1)

    fun getToday(formatText: String = DEFAULT_DATE_FORMAT_TEXT): String =
        getFormattedTextForDate(formatText, 0)

    fun getTomorrow(formatText: String = DEFAULT_DATE_FORMAT_TEXT): String =
        getFormattedTextForDate(formatText, 1)

    fun getCurrentYear(): Int {
        val formatter = SimpleDateFormat("yyyy", Locale.KOREA)
        val calendar =
            Calendar.getInstance(TimeZone.getTimeZone(DEFAULT_TIME_ZONE_ID))
        calendar.add(Calendar.DATE, 0)
        return formatter.format(calendar.time).toInt()
    }

    private fun getFormattedTextForDate(formatText: String, pivot: Int): String {
        val formatter = SimpleDateFormat(formatText, Locale.KOREA)
        val calendar = Calendar.getInstance(TimeZone.getTimeZone(DEFAULT_TIME_ZONE_ID))
        calendar.add(Calendar.DATE, pivot)
        return formatter.format(calendar.time)
    }
}