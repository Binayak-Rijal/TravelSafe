package com.example.travelsafe.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.travelsafe.model.Place
import com.example.travelsafe.repository.AdminPlaceRepo
import com.example.travelsafe.repository.AdminPlaceRepoImpl
import com.google.firebase.auth.FirebaseAuth

class AdminViewModel : ViewModel() {

    private val repo: AdminPlaceRepo = AdminPlaceRepoImpl()
    private val auth = FirebaseAuth.getInstance()

    // Admin email — change this to your actual admin email
    private val ADMIN_EMAIL = "admin@gmail.com"

    // Observable list of places
    val places = mutableStateListOf<Place>()
    var isLoading = false

    // ── Auth ────────────────────────────────────────────────

    fun isAdmin(): Boolean {
        return auth.currentUser?.email == ADMIN_EMAIL
    }

    fun adminLogin(
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit
    ) {
        if (email != ADMIN_EMAIL) {
            callback(false, "Not an admin account")
            return
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Admin login successful")
                } else {
                    callback(false, task.exception?.message ?: "Login failed")
                }
            }
    }

    fun adminLogout() {
        auth.signOut()
    }

    // ── CRUD ─────────────────────────────────────────────────

    fun loadAllPlaces(callback: (Boolean, String) -> Unit = { _, _ -> }) {
        repo.getAllPlaces { success, message, list ->
            if (success) {
                places.clear()
                places.addAll(list)
            }
            callback(success, message)
        }
    }

    fun addPlace(
        place: Place,
        callback: (Boolean, String) -> Unit
    ) {
        repo.addPlace(place) { success, message ->
            callback(success, message)
        }
    }

    fun updatePlace(
        place: Place,
        callback: (Boolean, String) -> Unit
    ) {
        repo.updatePlace(place) { success, message ->
            callback(success, message)
        }
    }

    fun deletePlace(
        placeId: String,
        callback: (Boolean, String) -> Unit
    ) {
        repo.deletePlace(placeId) { success, message ->
            if (success) {
                places.removeAll { it.placeId == placeId }
            }
            callback(success, message)
        }
    }

    fun searchPlaces(
        query: String,
        callback: (Boolean, String, List<Place>) -> Unit
    ) {
        repo.searchPlaces(query, callback)
    }
}