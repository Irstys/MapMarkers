package ru.netology.mapmarkers.dto

data class Point (
    val id: Long,
    val name: String,
    val longitude: Float,
    val latitude: Float,
    val content: String? = null,
    val viewed: Boolean = false,
  //  val attachment: Attachment? = null,

)