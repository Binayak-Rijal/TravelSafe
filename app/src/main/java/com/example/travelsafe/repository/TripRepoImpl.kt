package com.example.travelsafe.repository

import com.example.travelsafe.model.Trip
import com.example.travelsafe.utils.TripManager

class TripRepoImpl : TripRepo {

    override fun addTrip(
        trip: Trip,
        callback: (Boolean, String) -> Unit
    ) {
        return try {
            TripManager.addTrip(trip)
            callback(true, "Trip added successfully")
        } catch (e: Exception) {
            callback(false, e.message ?: "Failed to add trip")
        }
    }

    override fun removeTrip(
        trip: Trip,
        callback: (Boolean, String) -> Unit
    ) {
        return try {
            TripManager.removeTrip(trip)
            callback(true, "Trip removed successfully")
        } catch (e: Exception) {
            callback(false, e.message ?: "Failed to remove trip")
        }
    }

    override fun updateTripStatus(
        trip: Trip,
        status: String,
        callback: (Boolean, String) -> Unit
    ) {
        return try {
            TripManager.updateStatus(trip, status)
            callback(true, "Status updated to $status")
        } catch (e: Exception) {
            callback(false, e.message ?: "Failed to update status")
        }
    }

    override fun getAllTrips(
        callback: (Boolean, String, List<Trip>) -> Unit
    ) {
        return try {
            val trips = TripManager.trips.toList()
            callback(true, "Done", trips)
        } catch (e: Exception) {
            callback(false, e.message ?: "Failed to load trips", emptyList())
        }
    }

    override fun getUpcomingTrips(
        callback: (Boolean, String, List<Trip>) -> Unit
    ) {
        return try {
            val upcoming = TripManager.trips.filter {
                it.status == "Planned" || it.status == "Ongoing"
            }
            callback(true, "Done", upcoming)
        } catch (e: Exception) {
            callback(false, e.message ?: "Failed to load upcoming trips", emptyList())
        }
    }

    override fun getCompletedTrips(
        callback: (Boolean, String, List<Trip>) -> Unit
    ) {
        return try {
            val completed = TripManager.trips.filter {
                it.status == "Completed"
            }
            callback(true, "Done", completed)
        } catch (e: Exception) {
            callback(false, e.message ?: "Failed to load completed trips", emptyList())
        }
    }

    override fun getTripsByStatus(
        status: String,
        callback: (Boolean, String, List<Trip>) -> Unit
    ) {
        return try {
            val filtered = TripManager.trips.filter { it.status == status }
            callback(true, "Done", filtered)
        } catch (e: Exception) {
            callback(false, e.message ?: "Failed to load trips", emptyList())
        }
    }

    override fun getTripCount(): Int {
        return TripManager.trips.size
    }
}