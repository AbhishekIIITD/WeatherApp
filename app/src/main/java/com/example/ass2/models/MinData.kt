package com.example.ass2.models

data class MinData(
    val elevation: Int,
    val generationtime_ms: Double,
    val daily: MinDaily,
    val daily_units: MinDailyUnits,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timezone_abbreviation: String,
    val utc_offset_seconds: Int
)
