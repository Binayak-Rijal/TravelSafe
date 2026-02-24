package com.example.travelsafe.model

data class RecommendedPlace(
    val name: String,
    val imageUrl: String,
    val location: String = "",
    val description: String = "",
    val price: String = "",
    val rating: Float = 0f,
    val reviews: Int = 0,
    val highlights: List<String> = emptyList()
)