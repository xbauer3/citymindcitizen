package com.example.projectobcane.database.color

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ColorCategoryRepositoryImpl @Inject constructor(
    private val dao: ColorCategoryDao
) : IColorCategoryRepository {

    override suspend fun insert(colorCategory: ColorCategory): Long {
        return dao.insert(colorCategory)
    }

    override suspend fun update(colorCategory: ColorCategory) {
        dao.update(colorCategory)
    }

    override suspend fun delete(colorCategory: ColorCategory) {
        dao.delete(colorCategory)
    }

    override fun getAll(): Flow<List<ColorCategory>> {
        return dao.getAll()
    }

    override suspend fun getById(id: Long): ColorCategory? {
        return dao.getById(id)
    }
}