package com.example.travelsafe.repository

import com.example.travelsafe.model.Place

interface AdminPlaceRepo {

    fun addPlace(
        place: Place,
        callback: (Boolean, String) -> Unit
    )

    fun updatePlace(
        place: Place,
        callback: (Boolean, String) -> Unit
    )

    fun deletePlace(
        placeId: String,
        callback: (Boolean, String) -> Unit
    )

    fun getAllPlaces(
        callback: (Boolean, String, List<Place>) -> Unit
    )

    fun getPlaceById(
        placeId: String,
        callback: (Boolean, String, Place?) -> Unit
    )

    fun searchPlaces(
        query: String,
        callback: (Boolean, String, List<Place>) -> Unit
    )
}