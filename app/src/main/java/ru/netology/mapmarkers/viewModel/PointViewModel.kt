package ru.netology.mapmarkers.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.mapmarkers.db.AppDb
import ru.netology.mapmarkers.dto.PlacePoint
import ru.netology.mapmarkers.model.FeedModelState
import ru.netology.mapmarkers.repository.PointRepository
import ru.netology.mapmarkers.repository.PointRepositoryImpl
import ru.netology.mapmarkers.util.SingleLiveEvent

private val empty = PlacePoint(
    id = 0,
    content = "",
    name = "",
    latitude = 0.0,
    longitude = 0.0,
)

class PointViewModel(application: Application) : AndroidViewModel(application){
    private val repository: PointRepository = PointRepositoryImpl(
            AppDb.getInstance(application).pointDao()
    )
    val data = repository.getAll()
    private val _dataState = MutableLiveData(FeedModelState())
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    val edited: MutableLiveData<PlacePoint> = MutableLiveData(empty)
    private val _pointCreated = SingleLiveEvent<Unit>()
    val pointCreated: LiveData<Unit>
        get() = _pointCreated

    fun removeById(id: Long) = repository.removeById(id)

    fun edit(point: PlacePoint) {
        edited.value = point
    }
    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun saveContent() {
        edited.value?.let {
            repository.save(it)
            edited.value = empty
        }
    }

    fun save() {
        edited.value?.let {
            repository.save(it)
            edited.value = empty
        }
    }
    fun loadPoints()  {
            _dataState.value = FeedModelState(loading = true)
            repository.getAll()
            _dataState.value = FeedModelState()
    }

    fun refreshPoints() {
            _dataState.value = FeedModelState(refreshing = true)
            repository.getAll()
            _dataState.value = FeedModelState()
    }
}





