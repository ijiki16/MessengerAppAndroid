package com.ijiki16.messengerapp.infrastructure

import android.text.format.DateFormat
import java.util.*

fun Long.toHumanReadableDate(): String {
    val currentTime = System.currentTimeMillis()
    val timePassedInSec = (currentTime - this) / 1000
    return if(timePassedInSec < 60) {
        "1 min"
    } else if(timePassedInSec < 60 * 60) {
        val minutes = timePassedInSec / 60
        "$minutes min"
    } else if( timePassedInSec < 60 * 60 * 24) {
        val hrs = timePassedInSec / (60 * 60)
        "$hrs hours"
    } else {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = this * 1000L
        DateFormat.format("dd MMM",calendar).toString()
    }
}