package ru.netology.mapmarkers.model

import ru.netology.mapmarkers.dto.Point


data class FeedModel(
    val posts: List<Point> = emptyList(),
    val empty: Boolean = false,

    )