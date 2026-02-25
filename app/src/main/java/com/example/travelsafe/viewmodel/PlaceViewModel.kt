package com.example.travelsafe.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.travelsafe.model.RecommendedPlace
import com.example.travelsafe.repository.PlaceRepo
import com.example.travelsafe.repository.PlaceRepoImpl

class PlaceViewModel : ViewModel() {

    // Using RepoImpl through the interface
    private val placeRepo: PlaceRepo = PlaceRepoImpl()

    fun getAllPlaces(
        callback: (Boolean, String, List<RecommendedPlace>) -> Unit
    ) {
        placeRepo.getAllPlaces(callback)
    }

    fun getPlaceByName(
        name: String,
        callback: (Boolean, String, RecommendedPlace?) -> Unit
    ) {
        placeRepo.getPlaceByName(name, callback)
    }

    fun searchPlaces(
        query: String,
        callback: (Boolean, String, List<RecommendedPlace>) -> Unit
    ) {
        placeRepo.searchPlaces(query, callback)
    }

    fun getTopRatedPlaces(
        limit: Int = 3,
        callback: (Boolean, String, List<RecommendedPlace>) -> Unit
    ) {
        placeRepo.getTopRatedPlaces(limit, callback)
    }

    fun getPlacesByLocation(
        location: String,
        callback: (Boolean, String, List<RecommendedPlace>) -> Unit
    ) {
        placeRepo.getPlacesByLocation(location, callback)
    }

    fun uploadPlaceImage(
        context: Context,
        imageUri: Uri,
        callback: (String?) -> Unit
    ) {
        placeRepo.uploadPlaceImage(context, imageUri, callback)
    }
}