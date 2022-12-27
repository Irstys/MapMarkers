package ru.netology.mapmarkers.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.netology.mapmarkers.dao.PlacePointDao
import ru.netology.mapmarkers.dto.PlacePointEntity

@Database(entities = [PlacePointEntity::class], version = 1, exportSchema = false)
abstract class AppDb : RoomDatabase() {
    abstract fun pointDao(): PlacePointDao

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(context, AppDb::class.java, "place_db")
                    .build()
                    .also { instance = it }
            }
        }

    }
}