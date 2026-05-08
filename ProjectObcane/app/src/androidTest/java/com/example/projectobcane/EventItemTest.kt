package com.example.projectobcane

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.projectobcane.database.events.Event
import com.example.projectobcane.database.reports.LocationEntity
import com.example.projectobcane.screens.news.EventItem
import com.example.projectobcane.screens.news.EventWithWeather
import org.junit.Rule
import org.junit.Test



class EventItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()



    /**
     * Tests that an EventItem composable correctly displays
     * the event title and place name.
     */
    @Test
    fun eventItem_displaysTitleAndPlace() {
        val event = EventWithWeather(
            event = Event(
                id = 1L,
                title = "Festival",
                description = "",
                category = "MUSIC",
                status = "UPCOMING",
                placeName = "Park",
                date = System.currentTimeMillis(),
                location = LocationEntity(0.0, 0.0),
                photoUri = ""
            )
        )

        composeTestRule.setContent {
            androidx.compose.material3.MaterialTheme {
                EventItem(event = event, onClick = {})
            }
        }

        composeTestRule.waitForIdle()


        composeTestRule.onNodeWithText("Festival").assertIsDisplayed()


        composeTestRule.onNodeWithText("Park", substring = true).assertIsDisplayed()
    }



}