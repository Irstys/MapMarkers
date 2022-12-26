package ru.netology.mapmarkers.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.mapmarkers.db.AppDb
import ru.netology.mapmarkers.dto.PlacePoint
import ru.netology.mapmarkers.dto.PlacePointEntity

class MapsViewModel(context: Application) : AndroidViewModel(context) {
    private val dao = AppDb.getInstance(context).pointDao()
    val places = dao.getAll().map {
        it.map(PlacePointEntity::toDto)
    }
    fun insertPlace(pont: PlacePoint) {
        viewModelScope.launch {
            dao.insert(PlacePointEntity.fromDto(pont))
        }
    }
    fun deletePlaceById(id: Long) {
        viewModelScope.launch {
            dao.removeById(id)
        }
    }
}