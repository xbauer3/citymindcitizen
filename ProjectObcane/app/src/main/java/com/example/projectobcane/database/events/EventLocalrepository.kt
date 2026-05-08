package com.example.projectobcane.database.events

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class EventLocalRepositoryImpl @Inject constructor(
    private val dao: EventDao
) : IEventLocalRepository {


    override suspend fun insert(event: Event) {
        return dao.insert(event)
    }

    override suspend fun update(event: Event) {
        dao.update(event)
    }

    override suspend fun delete(event: Event) {
        dao.delete(event)
    }

    override fun getAll(): Flow<List<Event>> {
        return dao.getAll()
    }

    override suspend fun getById(id: Long): Event {
        return dao.getById(id)
    }

}
