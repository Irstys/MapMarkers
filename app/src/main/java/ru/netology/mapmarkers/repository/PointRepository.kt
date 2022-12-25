package ru.netology.mapmarkers.repository

import androidx.lifecycle.LiveData
import ru.netology.mapmarkers.dto.Point

interface PointRepository {
    fun getAll(): LiveData<List<Point>>
    fun save(post: Point)
    fun getPointById(id: Long): Point?
    fun removeById(id: Long)

}