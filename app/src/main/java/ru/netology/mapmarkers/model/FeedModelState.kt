package ru.netology.mapmarkers.model

import ru.netology.mapmarkers.dto.PlacePoint
import ru.netology.mapmarkers.util.RetryTypes

data class FeedModelState(
    val loading: Boolean = false,
    val error: Boolean = false,
    val refreshing: Boolean = false,
    //val messageOfCodeError: String = ""
    val retryId: Long = 0,
    val retryType: RetryTypes? = null,
    val retryPost: PlacePoint? = null,
)