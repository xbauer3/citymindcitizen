package com.example.projectobcane.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DateUtils {

    companion object {
        private val DATE_FORMAT_CS = "dd. MM. yyyy"
        private val DATE_FORMAT_EN = "yyyy/MM/dd"


        private val DATE_TIME_FORMAT_CS = "dd. MM. yyyy HH:mm"
        private val DATE_TIME_FORMAT_EN = "yyyy/MM/dd HH:mm"

        fun getDateString(unixTime: Long): String {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = unixTime

            val locale = Locale.getDefault()
            val pattern = if (locale.language == "cs") DATE_FORMAT_CS else DATE_FORMAT_EN

            val format = SimpleDateFormat(pattern, locale)
            return format.format(calendar.time)
        }


        fun getDateTimeString(unixTime: Long): String {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = unixTime

            val locale = Locale.getDefault()
            val pattern = if (locale.language == "cs") DATE_TIME_FORMAT_CS else DATE_TIME_FORMAT_EN

            val format = SimpleDateFormat(pattern, locale)
            return format.format(calendar.time)
        }
    }
}
