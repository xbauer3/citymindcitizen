package com.example.projectobcane.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projectobcane.database.events.Event
import com.example.projectobcane.database.events.EventDao
import com.example.projectobcane.database.reports.ReportDao
import com.example.projectobcane.database.reports.ReportEntity
import com.example.projectobcane.database.reports.ReportImageEntity

@Database(
    entities = [
        ReportEntity::class, ReportImageEntity::class,Event::class
    ],
    version = 17,
    exportSchema = true
)
abstract class ProjectDatabase : RoomDatabase() {
    abstract fun reportDao(): ReportDao
    abstract fun eventDao(): EventDao


    companion object {
        private var INSTANCE: ProjectDatabase? = null
        fun getDatabase(context: Context): ProjectDatabase {
            if (INSTANCE == null) {
                synchronized(ProjectDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            ProjectDatabase::class.java,
                            "project_database"
                        ).fallbackToDestructiveMigration().build()
                    }
                }
            }
            return INSTANCE!!
        }
    }

}
