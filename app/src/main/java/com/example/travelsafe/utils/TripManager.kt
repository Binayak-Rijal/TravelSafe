package com.example.travelsafe.utils

import androidx.compose.runtime.mutableStateListOf
import com.example.travelsafe.model.Trip
import java.util.UUID

object TripManager {
    val trips = mutableStateListOf<Trip>()

    fun addTrip(trip: Trip) {
        trips.add(trip.copy(id = UUID.randomUUID().toString()))
    }

    fun removeTrip(trip: Trip) {
        trips.removeAll { it.id == trip.id }
    }

    fun updateStatus(trip: Trip, status: String) {
        val index = trips.indexOfFirst { it.id == trip.id }
        if (index != -1) {
            trips[index] = trips[index].copy(status = status)
        }
    }
}