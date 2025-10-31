package com.example.projectobcane.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DateUtils {

companion object{
    private val DATE_FORMAT_CS = "dd. MM. yyyy"
    private val DATE_FORMAT_EN = "yyyy/MM/dd"

    fun getDateString(unixTime: Long): String{
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = unixTime


        val locale = Locale.getDefault()
        val pattern = if (locale.language == "cs") DATE_FORMAT_CS else DATE_FORMAT_EN

        val format = SimpleDateFormat(pattern, locale)
        return format.format(calendar.time)
    }


}

}