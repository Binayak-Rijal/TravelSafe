package com.example.travelsafe.viewmodel

import androidx.lifecycle.ViewModel
import com.example.travelsafe.model.Trip
import com.example.travelsafe.repository.TripRepo
import com.example.travelsafe.repository.TripRepoImpl

class TripViewModel : ViewModel() {

    // Using RepoImpl through the interface
    private val tripRepo: TripRepo = TripRepoImpl()

    fun addTrip(
        trip: Trip,
        callback: (Boolean, String) -> Unit
    ) {
        tripRepo.addTrip(trip, callback)
    }

    fun removeTrip(
        trip: Trip,
        callback: (Boolean, String) -> Unit
    ) {
        tripRepo.removeTrip(trip, callback)
    }

    fun updateTripStatus(
        trip: Trip,
        status: String,
        callback: (Boolean, String) -> Unit
    ) {
        tripRepo.updateTripStatus(trip, status, callback)
    }

    fun getAllTrips(
        callback: (Boolean, String, List<Trip>) -> Unit
    ) {
        tripRepo.getAllTrips(callback)
    }

    fun getUpcomingTrips(
        callback: (Boolean, String, List<Trip>) -> Unit
    ) {
        tripRepo.getUpcomingTrips(callback)
    }

    fun getCompletedTrips(
        callback: (Boolean, String, List<Trip>) -> Unit
    ) {
        tripRepo.getCompletedTrips(callback)
    }

    fun getTripsByStatus(
        status: String,
        callback: (Boolean, String, List<Trip>) -> Unit
    ) {
        tripRepo.getTripsByStatus(status, callback)
    }

    fun getTripCount(): Int {
        return tripRepo.getTripCount()
    }
}