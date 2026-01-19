package com.example.projectobcane.communication

import javax.inject.Inject



class WeatherRemoteRepositoryImpl @Inject constructor(
    private val api: OpenMeteoApi
) : IWeatherRemoteRepository {

    override suspend fun getWeatherCode(
        lat: Double,
        lng: Double,
        date: String
    ): CommunicationResult<Int> {

        val response = processResponse {
            api.getDailyWeather(
                latitude = lat,
                longitude = lng,
                startDate = date,
                endDate = date
            )
        }

        return when (response) {
            is CommunicationResult.Success -> {
                val code = response.data.daily.weather_code.firstOrNull()
                if (code != null) {
                    CommunicationResult.Success(code)
                } else {
                    CommunicationResult.Error(
                        CommunicationError(
                            code = 404,
                            message = "Weather code not found"
                        )
                    )
                }
            }

            is CommunicationResult.Error -> response
            is CommunicationResult.ConnectionError -> response
            is CommunicationResult.Exception -> response
        }
    }
}


