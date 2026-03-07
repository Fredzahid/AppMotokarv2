package com.fred.motocontrolapp.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    fun isToday(timestamp: Long): Boolean {
        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_YEAR)
        val year = calendar.get(Calendar.YEAR)
        
        calendar.timeInMillis = timestamp
        return today == calendar.get(Calendar.DAY_OF_YEAR) && year == calendar.get(Calendar.YEAR)
    }

    fun isYesterday(timestamp: Long): Boolean {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val yesterday = calendar.get(Calendar.DAY_OF_YEAR)
        val year = calendar.get(Calendar.YEAR)
        
        val itemCalendar = Calendar.getInstance()
        itemCalendar.timeInMillis = timestamp
        return yesterday == itemCalendar.get(Calendar.DAY_OF_YEAR) && year == itemCalendar.get(Calendar.YEAR)
    }

    fun isThisWeek(timestamp: Long): Boolean {
        val calendar = Calendar.getInstance()
        val week = calendar.get(Calendar.WEEK_OF_YEAR)
        val year = calendar.get(Calendar.YEAR)
        
        calendar.timeInMillis = timestamp
        return week == calendar.get(Calendar.WEEK_OF_YEAR) && year == calendar.get(Calendar.YEAR)
    }

    fun isThisMonth(timestamp: Long): Boolean {
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        
        calendar.timeInMillis = timestamp
        return month == calendar.get(Calendar.MONTH) && year == calendar.get(Calendar.YEAR)
    }
}
