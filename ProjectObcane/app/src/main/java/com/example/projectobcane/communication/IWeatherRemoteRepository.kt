package com.example.projectobcane.communication



interface IWeatherRemoteRepository : IBaseRemoteRepository {

    suspend fun getWeatherCode(
        lat: Double,
        lng: Double,
        date: String
    ): CommunicationResult<Int>
}
