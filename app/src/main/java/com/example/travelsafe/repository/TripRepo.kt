package com.example.travelsafe.repository

import com.example.travelsafe.model.Trip

interface TripRepo {

    fun addTrip(
        trip: Trip,
        callback: (Boolean, String) -> Unit
    )

    fun removeTrip(
        trip: Trip,
        callback: (Boolean, String) -> Unit
    )

    fun updateTripStatus(
        trip: Trip,
        status: String,
        callback: (Boolean, String) -> Unit
    )

    fun getAllTrips(
        callback: (Boolean, String, List<Trip>) -> Unit
    )

    fun getUpcomingTrips(
        callback: (Boolean, String, List<Trip>) -> Unit
    )

    fun getCompletedTrips(
        callback: (Boolean, String, List<Trip>) -> Unit
    )

    fun getTripsByStatus(
        status: String,
        callback: (Boolean, String, List<Trip>) -> Unit
    )

    fun getTripCount(): Int
}