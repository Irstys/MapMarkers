package ru.netology.mapmarkers.dto

data class PlacePoint (
    val id: Long=0,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val content: String? = null,

)