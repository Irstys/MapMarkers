package ru.netology.mapmarkers.model

import ru.netology.mapmarkers.dto.PlacePoint


data class FeedModel(
    val posts: List<PlacePoint> = emptyList(),
    val empty: Boolean = false,

    )