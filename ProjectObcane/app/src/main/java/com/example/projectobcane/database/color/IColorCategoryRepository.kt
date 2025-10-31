package com.example.projectobcane.database.color

import kotlinx.coroutines.flow.Flow

interface IColorCategoryRepository {
    suspend fun insert(colorCategory: ColorCategory): Long
    suspend fun update(colorCategory: ColorCategory)
    suspend fun delete(colorCategory: ColorCategory)
    fun getAll(): Flow<List<ColorCategory>>
    suspend fun getById(id: Long): ColorCategory?
}