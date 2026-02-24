package com.example.travelsafe.model

data class TripResult(
    val id: Int,
    val airline: String,
    val airlineLogoUrl: String,
    val origin: String,
    val originCode: String,
    val destination: String,
    val destinationCode: String,
    val departureTime: String,
    val arrivalTime: String,
    val duration: String,
    val price: String,
    val rating: Float,
    val reviews: Int,
    val type: String // "Flight", "Bus", "Train"
)