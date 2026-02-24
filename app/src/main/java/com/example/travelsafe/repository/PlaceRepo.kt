package com.example.travelsafe.repository

import android.content.Context
import android.net.Uri
import com.example.travelsafe.model.RecommendedPlace

interface PlaceRepo {

    fun getAllPlaces(
        callback: (Boolean, String, List<RecommendedPlace>) -> Unit
    )

    fun getPlaceByName(
        name: String,
        callback: (Boolean, String, RecommendedPlace?) -> Unit
    )

    fun searchPlaces(
        query: String,
        callback: (Boolean, String, List<RecommendedPlace>) -> Unit
    )

    fun getTopRatedPlaces(
        limit: Int,
        callback: (Boolean, String, List<RecommendedPlace>) -> Unit
    )

    fun getPlacesByLocation(
        location: String,
        callback: (Boolean, String, List<RecommendedPlace>) -> Unit
    )

    fun uploadPlaceImage(
        context: Context,
        imageUri: Uri,
        callback: (String?) -> Unit
    )
}