package com.example.travelsafe.model

data class SearchModel(
    val from: String = "",
    val to: String = "",
    val passengers: Int = 1,
    val departureDate: String = "",
    val returnDate: String = ""
)