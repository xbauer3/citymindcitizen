package com.example.projectobcane.database.color



import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ColorCategoryDao {
    @Insert
    suspend fun insert(colorCategory: ColorCategory): Long

    @Update
    suspend fun update(colorCategory: ColorCategory)

    @Delete
    suspend fun delete(colorCategory: ColorCategory)

    @Query("SELECT * FROM color_categories ORDER BY priority DESC")
    fun getAll(): Flow<List<ColorCategory>>

    @Query("SELECT * FROM color_categories WHERE id = :id")
    suspend fun getById(id: Long): ColorCategory?
}