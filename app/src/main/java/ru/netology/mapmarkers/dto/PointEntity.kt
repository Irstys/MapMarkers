package ru.netology.mapmarkers.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "points")
data class PointEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val name: String,
        val longitude: Float,
        val latitude: Float,
        val content: String? = null,
        val viewed: Boolean = false,
     //   val attachment: Attachment? = null,
    ){
    fun toDto() = Point(
        id,
        name,
        longitude,
        latitude,
        content,
        viewed,
      //  attachment
        )


    companion object {
        fun fromDto(dto: Point) =
            PointEntity(
                dto.id,
                dto.name,
                dto.longitude,
                dto.latitude,
                dto.content,
                dto.viewed,
             //   dto.attachment
            )
    }
}