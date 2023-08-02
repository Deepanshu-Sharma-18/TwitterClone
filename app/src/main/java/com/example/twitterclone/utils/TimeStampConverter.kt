package com.example.twitterclone.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun formatTimestamp(timestamp: Long): String {
    val dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
    val dateFormatter = DateTimeFormatter.ofPattern("MM:dd")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val formattedDate = dateFormatter.format(dateTime)
    val formattedTime = timeFormatter.format(dateTime)
    return "$formattedDate $formattedTime"
}
