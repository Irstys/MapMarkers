package ru.netology.mapmarkers.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.netology.mapmarkers.dto.AttachmentType
import ru.netology.mapmarkers.dto.PointEntity

@Dao
interface PointDao {
    @Query("SELECT * FROM points ORDER BY id DESC")
    fun getAll(): LiveData<List<PointEntity>>
    @Query("SELECT COUNT(*) == 0 FROM points")
    fun isEmpty(): Boolean
    @Query("SELECT COUNT(*) FROM points")
    fun count(): Int
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: PointEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: List<PointEntity>)
    @Query("DELETE FROM points WHERE id = :id")
    fun removeById(id: Long)
    @Query("UPDATE points SET viewed = 1 WHERE viewed = 0")
    fun viewedPoints()
    @Query("SELECT * FROM points WHERE id = :id")
    fun getPointById(id: Long): PointEntity?
    @Query("UPDATE points SET content = :content WHERE id = :id")
    fun updateContentById(id: Long, content: String)
    fun save(point: PointEntity) {
        if (point.id == 0L) insert(point) else point.content?.let {
            updateContentById(point.id,
                it)
        }
    }
}
class Converters {
    @TypeConverter
    fun toAttachmentType(value: String) = enumValueOf<AttachmentType>(value)

    @TypeConverter
    fun fromAttachmentType(value: AttachmentType) = value.name
}