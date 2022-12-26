package ru.netology.mapmarkers.repository

import androidx.lifecycle.Transformations
import ru.netology.mapmarkers.dao.PlacePointDao
import ru.netology.mapmarkers.dto.*



class PointRepositoryImpl (private val placePointDao: PlacePointDao) : PointRepository {
    override fun getAll() = placePointDao.getAll()


   override fun getPointById(id: Long): PlacePoint? = placePointDao.getPointById(id)?.toDto()

    override fun removeById(id: Long) {
        placePointDao.removeById(id)
    }

    override fun save(point: PlacePoint) {
        placePointDao.insert(PlacePointEntity.fromDto(point))

    }
}


