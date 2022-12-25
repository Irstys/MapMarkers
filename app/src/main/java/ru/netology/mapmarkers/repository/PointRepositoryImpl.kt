package ru.netology.mapmarkers.repository

import androidx.lifecycle.Transformations
import ru.netology.mapmarkers.dao.PointDao
import ru.netology.mapmarkers.dto.*
import javax.inject.Singleton


@Singleton
class PointRepositoryImpl (private val pointDao: PointDao) : PointRepository {
    override fun getAll() = Transformations.map(pointDao.getAll()) { list ->
        list.map {
            it.toDto()
        }
    }

   override fun getPointById(id: Long): Point? = pointDao.getPointById(id)?.toDto()

    override fun removeById(id: Long) {
        pointDao.removeById(id)
    }

    override fun save(point: Point) {
        pointDao.save(PointEntity.fromDto(point))

    }
}


