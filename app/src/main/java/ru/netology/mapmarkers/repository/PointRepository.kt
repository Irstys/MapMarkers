package ru.netology.mapmarkers.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import ru.netology.mapmarkers.dto.PlacePoint
import ru.netology.mapmarkers.dto.PlacePointEntity

interface PointRepository {
    fun getAll(): Flow<List<PlacePointEntity>>
    fun save(post: PlacePoint)
    fun getPointById(id: Long): PlacePoint?
    fun removeById(id: Long)

}