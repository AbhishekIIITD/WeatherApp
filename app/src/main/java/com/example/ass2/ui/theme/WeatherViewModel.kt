package com.example.ass2.ui.theme


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ass2.DataEvents
import com.example.ass2.Weather
import com.example.ass2.models.MaxData
import com.example.ass2.models.MinData
import com.example.ass2.models.dataState
import com.example.ass2.network.WeatherApi
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface WeatherUiState {
    data class Success(val maxData: MaxData?, val minData:MinData?) : WeatherUiState
    object Error : WeatherUiState
    object Loading : WeatherUiState
    data class DbSuccess(val date:String,val maxData:Double,val minData:Double):WeatherUiState
}

sealed interface DbState {
    data class Success(val weather: Weather) : DbState
    object Error : DbState
    object Loading : DbState
}

class WeatherViewModel : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var weatherUiState: WeatherUiState by mutableStateOf(WeatherUiState.Loading)
    var dbState:DbState by mutableStateOf(DbState.Loading)
    /**
     * Call getMarsPhotos() on init so we can display status immediately.
     */
    init {
        weatherUiState = WeatherUiState.Loading
        dbState=DbState.Loading
    }

    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [MarsPhoto] [List] [MutableList].
     */





    fun getCurrentWeather( lat:Double, long:Double, start:String, end:String) {

        viewModelScope.launch {
            weatherUiState = try {
                val listResult_Max = WeatherApi.retrofitService.getMaxTemperature(lat,long,start,end,"temperature_2m_max")
                val listResult_Min = WeatherApi.retrofitService.getMinTemperature(lat,long,start,end,"temperature_2m_min")
//                Log.d("Main",listResult_Min.toString())
//                Log.d("Main",listResult_Max.toString())
                WeatherUiState.Success(
                    listResult_Max.body(),
                    listResult_Min.body()
                )
            } catch (e: IOException) {
                Log.d("main",e.toString())
                WeatherUiState.Error
            } catch (e: HttpException) {
                Log.d("main",e.toString())
                WeatherUiState.Error
            }
        }


    }

}
