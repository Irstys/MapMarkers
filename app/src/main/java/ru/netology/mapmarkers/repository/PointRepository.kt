package ru.netology.mapmarkers.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.mapmarkers.dto.PlacePoint

interface PointRepository {
        val data: Flow<List<PlacePoint>>
        suspend fun getMarker(id: Long): PlacePoint
        suspend fun remove(id: Long)
        suspend fun save(point: PlacePoint): Long

}