package com.example.travelsafe.model

data class Place(
    val placeId: String = "",
    val name: String = "",
    val location: String = "",
    val description: String = "",
    val price: String = "",
    val rating: Float = 0f,
    val reviews: Int = 0,
    val imageUrl: String = "",
    val highlights: List<String> = emptyList(),
    val category: String = "General" // General, Beach, Mountain, Culture, Food
)