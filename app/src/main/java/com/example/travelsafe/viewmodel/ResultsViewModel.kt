package com.example.travelsafe.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.travelsafe.model.TripResult

class ResultsViewModel : ViewModel() {

    var selectedFilter by mutableStateOf("All")
        private set

    var sortBy by mutableStateOf("Price")
        private set

    val filters = listOf("All", "Flight", "Bus", "Train")
    val sortOptions = listOf("Price", "Duration", "Rating")

    val allResults = listOf(
        TripResult(
            id = 1,
            airline = "Delta Airlines",
            airlineLogoUrl = "https://res.cloudinary.com/YOUR_CLOUD_NAME/image/upload/delta_logo.png",
            origin = "New York", originCode = "JFK",
            destination = "Los Angeles", destinationCode = "LAX",
            departureTime = "08:00", arrivalTime = "11:45",
            duration = "3h 45m", price = "$170.6",
            rating = 4.5f, reviews = 128, type = "Flight"
        ),
        TripResult(
            id = 2,
            airline = "United Airlines",
            airlineLogoUrl = "https://res.cloudinary.com/YOUR_CLOUD_NAME/image/upload/united_logo.png",
            origin = "New York", originCode = "JFK",
            destination = "Los Angeles", destinationCode = "LAX",
            departureTime = "10:30", arrivalTime = "14:00",
            duration = "3h 30m", price = "$210.0",
            rating = 4.2f, reviews = 95, type = "Flight"
        ),
        TripResult(
            id = 3,
            airline = "City Bus Co.",
            airlineLogoUrl = "https://res.cloudinary.com/YOUR_CLOUD_NAME/image/upload/bus_logo.png",
            origin = "New York", originCode = "NYC",
            destination = "Los Angeles", destinationCode = "LAX",
            departureTime = "07:00", arrivalTime = "15:00",
            duration = "8h 00m", price = "$55.0",
            rating = 3.8f, reviews = 60, type = "Bus"
        ),
        TripResult(
            id = 4,
            airline = "Amtrak",
            airlineLogoUrl = "https://res.cloudinary.com/YOUR_CLOUD_NAME/image/upload/amtrak_logo.png",
            origin = "New York", originCode = "NYC",
            destination = "Los Angeles", destinationCode = "LAX",
            departureTime = "06:00", arrivalTime = "14:30",
            duration = "8h 30m", price = "$89.0",
            rating = 4.0f, reviews = 75, type = "Train"
        ),
        TripResult(
            id = 5,
            airline = "Southwest",
            airlineLogoUrl = "https://res.cloudinary.com/YOUR_CLOUD_NAME/image/upload/southwest_logo.png",
            origin = "New York", originCode = "JFK",
            destination = "Los Angeles", destinationCode = "LAX",
            departureTime = "13:00", arrivalTime = "16:30",
            duration = "3h 30m", price = "$145.0",
            rating = 4.3f, reviews = 110, type = "Flight"
        )
    )

    val filteredResults: List<TripResult>
        get() {
            val filtered = if (selectedFilter == "All") allResults
            else allResults.filter { it.type == selectedFilter }

            return when (sortBy) {
                "Price" -> filtered.sortedBy { it.price.removePrefix("$").toDoubleOrNull() ?: 0.0 }
                "Duration" -> filtered.sortedBy { it.duration }
                "Rating" -> filtered.sortedByDescending { it.rating }
                else -> filtered
            }
        }

    fun setFilter(filter: String) { selectedFilter = filter }
    fun setSort(sort: String) { sortBy = sort }
}