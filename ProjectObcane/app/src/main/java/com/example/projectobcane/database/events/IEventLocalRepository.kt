package com.example.projectobcane.database.events

import kotlinx.coroutines.flow.Flow



interface IEventLocalRepository {
    suspend fun insert(event: Event)
    suspend fun update(event: Event)
    suspend fun delete(event: Event)
    fun getAll(): Flow<List<Event>>
    suspend fun getById(id: Long): Event
}