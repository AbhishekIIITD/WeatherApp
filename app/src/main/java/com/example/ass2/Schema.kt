package com.example.ass2

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity
data class Weather(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "date") val date: String?,
    val minTempData: Double,
    val maxTempData: Double,

    )


@Dao
interface WeatherDao {
    @Query("SELECT * FROM Weather")
    fun getAll(): Flow<List<Weather>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(data: Weather)

    @Query("Select * From weather where date=:date")
    fun getTemp(date:String):Flow<List<Weather>>
}
