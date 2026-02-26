package com.example.travelsafe.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import com.example.travelsafe.model.Place
import com.example.travelsafe.repository.FirebasePlaceRepoImpl


object FirebaseListenerManager {

    fun fetchPlaces(
        repo: FirebasePlaceRepoImpl,
        onDataReceived: (List<Place>) -> Unit,
        onError: (String) -> Unit
    ) {
        repo.getAllPlaces { success, error, places ->
            if (success) {
                onDataReceived(places)
            } else {
                onError(error ?: "Unknown error")
            }
        }
    }
}

