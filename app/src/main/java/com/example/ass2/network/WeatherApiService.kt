package com.example.ass2.network

import com.example.ass2.models.MaxData
import com.example.ass2.models.MinData
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL =
    "https://archive-api.open-meteo.com/v1/"
//https://archive-api.open-meteo.com/v1/archive?latitude=22&longitude=79&start_date=2024-03-25&end_date=2024-03-26&daily=temperature_2m

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface WeatherApiService{
    @GET("archive")
    suspend fun getMaxTemperature(@Query("latitude") lat:Double,@Query("longitude") long:Double,@Query("start_date") start:String,@Query("end_date") end:String,@Query("daily") daily:String):Response<MaxData>

    @GET("archive")
    suspend fun getMinTemperature(@Query("latitude") lat:Double,@Query("longitude") long:Double,@Query("start_date") start:String,@Query("end_date") end:String,@Query("daily") daily:String):Response<MinData>

}

object WeatherApi {
    val retrofitService=retrofit.create(WeatherApiService::class.java)

}