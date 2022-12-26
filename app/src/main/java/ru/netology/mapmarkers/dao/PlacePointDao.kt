package ru.netology.mapmarkers.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.netology.mapmarkers.dto.PlacePointEntity


@Dao
interface PlacePointDao {
    @Query("SELECT * FROM points ORDER BY id DESC")
    fun getAll(): Flow<List<PlacePointEntity>>
    @Query("SELECT COUNT(*) == 0 FROM points")
    suspend fun isEmpty(): Boolean
    @Query("SELECT COUNT(*) FROM points")
    suspend fun count(): Int
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(point: PlacePointEntity)
    @Query("DELETE FROM points WHERE id = :id")
    fun removeById(id: Long)
    @Query("SELECT * FROM points WHERE id = :id")
    fun getPointById(id: Long): PlacePointEntity?
    @Query("UPDATE points SET content = :content WHERE id = :id")
    fun updateContentById(id: Long, content: String)
    suspend fun save(point: PlacePointEntity) {
        if (point.id == 0L) insert(point) else point.content?.let {
            updateContentById(point.id,
                it)
        }
    }
}
