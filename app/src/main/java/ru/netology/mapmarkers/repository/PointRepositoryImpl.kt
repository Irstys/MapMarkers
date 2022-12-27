package ru.netology.mapmarkers.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.netology.mapmarkers.dao.PlacePointDao
import ru.netology.mapmarkers.dto.PlacePoint
import ru.netology.mapmarkers.dto.PlacePointEntity
import ru.netology.mapmarkers.dto.toDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PointRepositoryImpl  @Inject constructor(
    private val dao: PlacePointDao
) : PointRepository {
    override val data = dao.getAll()
        .map(List<PlacePointEntity>::toDto)
        .flowOn(Dispatchers.Default)

    override suspend fun getMarker(id: Long) = dao.getMarker(id).toDto()

    override suspend fun remove(id: Long) = dao.removeById(id)

    override suspend fun save(point: PlacePoint): Long = dao.insert(PlacePointEntity.fromDto(point))

}