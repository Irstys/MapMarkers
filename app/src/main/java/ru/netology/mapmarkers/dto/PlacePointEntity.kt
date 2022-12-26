package ru.netology.mapmarkers.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "points")
data class PlacePointEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0,
        val name: String,
        val latitude: Double,
        val longitude: Double,
        val content: String? = null,
    ) {
    fun toDto() = PlacePoint(
        id,
        name,
        latitude,
        longitude,
        content,
    )


    companion object {
        fun fromDto(dto: PlacePoint) =
            PlacePointEntity(
                dto.id,
                dto.name,
                dto.latitude,
                dto.longitude,
                dto.content,
            )
    }
}