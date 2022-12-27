package ru.netology.mapmarkers.viewModel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.mapmarkers.db.AppDb
import ru.netology.mapmarkers.dto.PlacePoint
import ru.netology.mapmarkers.dto.PlacePointEntity
import ru.netology.mapmarkers.repository.PointRepository
import ru.netology.mapmarkers.util.SingleLiveEvent

private val empty = PlacePoint(
    id = 0L,
    name = "",
    latitude = 0.0,
    longitude = 0.0
)
class MapsViewModel(
    context: Application,
    private val repository: PointRepository
    ) : AndroidViewModel(context) {

    val data: LiveData<List<PlacePoint>> =
        repository.data.asLiveData(Dispatchers.Default)
    private val dao = AppDb.getInstance(context).pointDao()
    val places = dao.getAll().map {
        it.map(PlacePointEntity::toDto)
    }

    val edited = MutableLiveData(empty)

    private val _pointCreated = SingleLiveEvent<Unit>()

        val pointCreated: LiveData<Unit>
        get() = _pointCreated

    fun insertPlace(point: PlacePoint) {
        viewModelScope.launch {
            repository.save(point)
        }
    }

    fun save() = viewModelScope.launch {
        edited.value?.let { repository.save(it) }
        edited.value = empty
    }

    fun cancelEdit() {
        edited.value = empty
    }
    fun edit(point: PlacePoint) {
        edited.value = point
    }

    fun deletePlaceById(id: Long) {
        viewModelScope.launch {
            repository.remove(id)
        }
    }
    fun getMarker(id: Long): LiveData<PlacePoint> {
        val point = MutableLiveData<PlacePoint>()
        viewModelScope.launch {
            point.postValue(repository.getMarker(id))
        }
        return point
    }
}