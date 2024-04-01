package com.example.ass2

sealed interface DataEvents{
    data class addData(val data: Weather) : DataEvents
}