package com.example.ass2.ui.theme

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ass2.DataEvents


import com.example.ass2.Weather
import com.example.ass2.WeatherDao
import com.example.ass2.models.dataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch




class WeatherDataViewModel(private val dao: WeatherDao) : ViewModel() {
    val data=dao.getAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun OnEvent(event:DataEvents){
        when(event){
            is DataEvents.addData -> {
                viewModelScope.launch { dao.insertData(event.data) }
            }

        }
    }
}

