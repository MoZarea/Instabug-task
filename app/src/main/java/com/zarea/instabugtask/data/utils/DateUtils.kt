package com.zarea.instabugtask.data.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getTodayDate(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(Date())
}