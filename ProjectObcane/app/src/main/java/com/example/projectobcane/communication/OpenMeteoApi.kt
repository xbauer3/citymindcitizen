package com.example.projectobcane.communication

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response


interface OpenMeteoApi {

    @GET("v1/forecast")
    suspend fun getDailyWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("daily") daily: String = "weather_code",
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("timezone") timezone: String = "auto"
    ): Response<WeatherResponse>
}
