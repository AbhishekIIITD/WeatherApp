package com.example.ass2.models

data class MaxData(
    val elevation: Int,
    val generationtime_ms: Double,
    val daily: Daily,
    val daily_units: DailyUnits,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timezone_abbreviation: String,
    val utc_offset_seconds: Int
)