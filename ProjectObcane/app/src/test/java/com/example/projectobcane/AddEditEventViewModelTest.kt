package com.example.projectobcane

import com.example.projectobcane.database.events.Event
import com.example.projectobcane.database.events.IEventLocalRepository
import com.example.projectobcane.screens.events.addEdit.AddEditEventViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class AddEditEventViewModelTest {

    private lateinit var repository: FakeEventRepository
    private lateinit var viewModel: AddEditEventViewModel

    @Before
    fun setup() {
        repository = FakeEventRepository()
        viewModel = AddEditEventViewModel(repository)
    }


    /**
     * Tests that updating the event title
     * correctly updates the UI state.
     */
    @Test
    fun onTitleChanged_updatesTitle() {
        viewModel.onTitleChanged("Event name")

        assertEquals(
            "Event name",
            viewModel.addEditEventUIState.value.event.title
        )
    }


    /**
     * Tests that attempting to save an event with
     * an empty title sets a validation error.
     */
    @Test
    fun saveEvent_withEmptyTitle_setsError() {
        viewModel.onTitleChanged("")
        viewModel.saveEvent()

        assertNotNull(viewModel.addEditEventUIState.value.titleError)
    }
}


/**
 * Fake repository for testing AddEditEventViewModel.
 * Stores events in a simple mutable list.
 */
class FakeEventRepository(
    initialEvents: List<Event> = emptyList()
) : IEventLocalRepository {

    private val events = initialEvents.toMutableList()

    override suspend fun insert(event: Event) {
        events.add(event)
    }

    override suspend fun update(event: Event) {}

    override suspend fun delete(event: Event) {
        events.remove(event)
    }

    override fun getAll(): Flow<List<Event>> = flow {
        emit(events)
    }

    override suspend fun getById(id: Long): Event {
        return events.first { it.id == id }
    }
}


