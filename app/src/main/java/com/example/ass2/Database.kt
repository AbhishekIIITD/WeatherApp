package com.example.ass2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Weather::class], version = 1, exportSchema = false)
abstract class InventoryDatabase : RoomDatabase() {

    abstract val Dao: WeatherDao


}