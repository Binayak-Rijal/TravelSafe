package com.example.travelsafe.model

data class AppNotification(
    val id: Int,
    val title: String,
    val message: String,
    val time: String,
    val isRead: Boolean = false,
    val type: String = "info" // info, booking, promo, alert
)