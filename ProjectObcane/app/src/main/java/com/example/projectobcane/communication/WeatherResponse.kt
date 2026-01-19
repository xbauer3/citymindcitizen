package com.example.projectobcane.communication


data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val daily: DailyWeather
)

data class DailyWeather(
    val time: List<String>,
    val weather_code: List<Int>
)
