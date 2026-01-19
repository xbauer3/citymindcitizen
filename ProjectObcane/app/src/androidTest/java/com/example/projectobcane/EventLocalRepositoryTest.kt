package com.example.projectobcane

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.projectobcane.database.ProjectDatabase
import com.example.projectobcane.database.events.Event
import com.example.projectobcane.database.events.EventCategory
import com.example.projectobcane.database.events.EventDao
import com.example.projectobcane.database.events.EventLocalRepositoryImpl
import com.example.projectobcane.database.events.EventStatus
import com.example.projectobcane.database.events.IEventLocalRepository
import com.example.projectobcane.database.reports.LocationEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EventLocalRepositoryTest {

    private lateinit var db: ProjectDatabase
    private lateinit var dao: EventDao
    private lateinit var repository: IEventLocalRepository

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ProjectDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = db.eventDao()
        repository = EventLocalRepositoryImpl(dao)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertEvent_andGetAll_returnsEvent() = runTest {
        val event = Event(
            title = "Concert",
            description = "Live music",
            category = EventCategory.MUSIC.name,
            status = EventStatus.UPCOMING.name,
            placeName = "Square",
            date = 1000L,
            location = LocationEntity(49.2, 16.6),
            photoUri = ""
        )

        repository.insert(event)

        val events = repository.getAll().first()

        assertEquals(1, events.size)
        assertEquals("Concert", events.first().title)
    }
}
