package ru.netology.mapmarkers.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.netology.mapmarkers.dto.PlacePointEntity


@Dao
interface PlacePointDao {
    @Query("SELECT * FROM points")
    fun getAll(): Flow<List<PlacePointEntity>>
    @Query("SELECT COUNT(*) == 0 FROM points")
    fun isEmpty(): Boolean
    @Query("SELECT * FROM points WHERE id = :id")
    suspend fun getMarker(id: Long): PlacePointEntity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(point: PlacePointEntity): Long
    @Query("DELETE FROM points WHERE id = :id")
    fun removeById(id: Long)
    @Query("SELECT * FROM points WHERE id = :id")
    fun getPointById(id: Long): PlacePointEntity?

}
