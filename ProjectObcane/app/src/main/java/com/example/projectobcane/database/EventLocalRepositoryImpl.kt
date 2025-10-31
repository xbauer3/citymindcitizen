package com.example.projectobcane.database

import com.example.projectobcane.navigation.EventLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject


class EventLocalRepositoryImpl @Inject constructor(private val eventDao: EventDao) : IEventLocalRepository {

    override suspend fun insert(event: Event) {
        return eventDao.insert(event)
    }

    override fun getAll(): Flow<List<EventWithColorCategory>> {
        return eventDao.getAll()
    }

    override suspend fun update(event: Event) {
        eventDao.update(event)
    }

    override suspend fun delete(event: Event) {
        eventDao.delete(event)
    }


    override suspend fun getById(id: Long): EventWithColorCategory {
        return eventDao.getById(id)
    }

    override suspend fun getEventLocations(): List<EventLocation> {
        return eventDao.getAll()  // This returns Flow<List<EventWithColorCategory>>
            .first()              // Collect the latest value (suspend)
            .filter {
                it.event.location.latitude != null && it.event.location.longitude != null
            }
            .filter {
                it.event.location.latitude != 0.0 || it.event.location.longitude != 0.0
            }
            .map {
                EventLocation(
                    latitude = it.event.location.latitude!!,
                    longitude = it.event.location.longitude!!,
                    name = it.event.eventName
                )
            }
    }

}