package com.example.travelsafe.model

data class Booking(
    val bookingId: String = "",
    val userId: String = "",
    val userEmail: String = "",
    val userName: String = "",
    val placeId: String = "",
    val placeName: String = "",
    val placeLocation: String = "",
    val placeImageUrl: String = "",
    val checkInDate: String = "",
    val checkOutDate: String = "",
    val guests: Int = 1,
    val totalPrice: String = "",
    val specialRequests: String = "",
    val status: String = "Pending", // Pending, Confirmed, Cancelled
    val bookedAt: String = ""
)