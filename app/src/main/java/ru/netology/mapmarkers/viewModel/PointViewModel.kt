package ru.netology.mapmarkers.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.mapmarkers.db.AppDb
import ru.netology.mapmarkers.dto.Point
import ru.netology.mapmarkers.model.FeedModelState
import ru.netology.mapmarkers.repository.PointRepository
import ru.netology.mapmarkers.repository.PointRepositoryImpl
import ru.netology.mapmarkers.util.SingleLiveEvent

private val empty = Point(
    id = 0,
    content = "",
    name = "",
    longitude = 0F,
    latitude = 0F,
    viewed = false,
)

class PointViewModel(application: Application) : AndroidViewModel(application){
    private val repository: PointRepository = PointRepositoryImpl(
            AppDb.getInstance(application).pointDao()
    )
    val data = repository.getAll()
    private val _dataState = MutableLiveData(FeedModelState())
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    val edited: MutableLiveData<Point> = MutableLiveData(empty)
    private val _pointCreated = SingleLiveEvent<Unit>()
    val pointCreated: LiveData<Unit>
        get() = _pointCreated

    fun removeById(id: Long) = repository.removeById(id)

    fun edit(point: Point) {
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





