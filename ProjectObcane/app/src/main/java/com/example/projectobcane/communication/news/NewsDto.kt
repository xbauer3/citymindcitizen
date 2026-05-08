package com.example.projectobcane.communication.news

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class NewsItemDto(
    @Json(name = "id") val id: Long,
    @Json(name = "localizedAttributes") val localizedAttributes: Map<String, LocalizedAttributesDto>,
    @Json(name = "created") val created: Long,
    @Json(name = "lastUpdate") val lastUpdate: Long,
    @Json(name = "startDate") val startDate: String?,
    @Json(name = "endDate") val endDate: String?,
    @Json(name = "category") val category: List<CategoryDto>,
    @Json(name = "audience") val audience: List<AudienceDto>,
    @Json(name = "faculty") val faculty: List<FacultyDto>,
    @Json(name = "isArchived") val isArchived: Boolean,
    @Json(name = "isConcept") val isConcept: Boolean,
    @Json(name = "titleImageUrl") val titleImageUrl: String?
)

@JsonClass(generateAdapter = true)
data class LocalizedAttributesDto(
    @Json(name = "title") val title: String?,
    @Json(name = "text") val text: String?,
    @Json(name = "unformattedText") val unformattedText: String?,
    @Json(name = "customPlace") val customPlace: String?
)

@JsonClass(generateAdapter = true)
data class CategoryDto(
    @Json(name = "id") val id: Int,
    @Json(name = "localizedAttributes") val localizedAttributes: Map<String, CategoryNameDto>,
    @Json(name = "iconName") val iconName: String
)

@JsonClass(generateAdapter = true)
data class CategoryNameDto(
    @Json(name = "name") val name: String
)

@JsonClass(generateAdapter = true)
data class AudienceDto(
    @Json(name = "id") val id: Int,
    @Json(name = "localizedAttributes") val localizedAttributes: Map<String, AudienceNameDto>,
    @Json(name = "iconName") val iconName: String
)

@JsonClass(generateAdapter = true)
data class AudienceNameDto(
    @Json(name = "name") val name: String
)

@JsonClass(generateAdapter = true)
data class FacultyDto(
    @Json(name = "id") val id: Int,
    @Json(name = "localizedAttributes") val localizedAttributes: Map<String, FacultyAttributesDto>,
    @Json(name = "imageUrl") val imageUrl: String?,
    @Json(name = "imageUrlDark") val imageUrlDark: String?
)

@JsonClass(generateAdapter = true)
data class FacultyAttributesDto(
    @Json(name = "name") val name: String,
    @Json(name = "type") val type: String,
    @Json(name = "description") val description: String
)