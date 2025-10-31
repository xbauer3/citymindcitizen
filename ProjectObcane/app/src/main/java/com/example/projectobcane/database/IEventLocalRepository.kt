package com.example.projectobcane.database

import com.example.projectobcane.navigation.EventLocation
import kotlinx.coroutines.flow.Flow



interface IEventLocalRepository {
    suspend fun insert(event: Event)
    fun getAll(): Flow<List<EventWithColorCategory>>
    suspend fun update(event: Event)
    suspend fun delete(event: Event)
    suspend fun getById(id: Long): EventWithColorCategory

    suspend fun getEventLocations(): List<EventLocation>
}