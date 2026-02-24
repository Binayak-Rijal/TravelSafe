package com.example.travelsafe.model

data class Trip(
    val id: String = "",
    val destination: String = "",
    val departureDate: String = "",
    val returnDate: String = "",
    val passengers: Int = 1,
    val notes: String = "",
    val status: String = "Planned" // Planned, Ongoing, Completed
)