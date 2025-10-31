package com.example.projectobcane.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projectobcane.database.color.ColorCategory
import com.example.projectobcane.database.color.ColorCategoryDao


@Database(entities = [Event::class, ColorCategory::class], version = 10, exportSchema = true)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun colorCategoryDao(): ColorCategoryDao

    companion object {
        private var INSTANCE: EventDatabase? = null
        fun getDatabase(context: Context): EventDatabase {
            if (INSTANCE == null) {
                synchronized(EventDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            EventDatabase::class.java,
                            "events_database"
                        ).fallbackToDestructiveMigration().build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}