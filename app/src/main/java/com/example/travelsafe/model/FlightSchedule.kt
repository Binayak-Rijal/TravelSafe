package com.example.travelsafe.model

data class FlightSchedule(
    val airline: String,
    val airlineLogoUrl: String,
    val origin: String,
    val originCode: String,
    val destination: String,
    val destinationCode: String,
    val duration: String,
    val price: String,
    val rating: Int
)