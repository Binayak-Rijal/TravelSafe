package com.example.travelsafe.repository

import com.example.travelsafe.model.Place
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminPlaceRepoImpl : AdminPlaceRepo {

    // Firebase Realtime Database reference
    private val db = FirebaseDatabase.getInstance()
    private val ref = db.getReference("places")

    override fun addPlace(
        place: Place,
        callback: (Boolean, String) -> Unit
    ) {
        // Generate unique ID from Firebase
        val placeId = ref.push().key ?: run {
            callback(false, "Failed to generate ID")
            return
        }

        val newPlace = place.copy(placeId = placeId)

        ref.child(placeId).setValue(newPlace)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Place added successfully")
                } else {
                    callback(false, task.exception?.message ?: "Failed to add place")
                }
            }
    }

    override fun updatePlace(
        place: Place,
        callback: (Boolean, String) -> Unit
    ) {
        if (place.placeId.isEmpty()) {
            callback(false, "Invalid place ID")
            return
        }

        ref.child(place.placeId).setValue(place)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Place updated successfully")
                } else {
                    callback(false, task.exception?.message ?: "Failed to update place")
                }
            }
    }

    override fun deletePlace(
        placeId: String,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(placeId).removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Place deleted successfully")
                } else {
                    callback(false, task.exception?.message ?: "Failed to delete place")
                }
            }
    }

    override fun getAllPlaces(
        callback: (Boolean, String, List<Place>) -> Unit
    ) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val places = snapshot.children.mapNotNull {
                    it.getValue(Place::class.java)
                }
                callback(true, "Done", places)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, emptyList())
            }
        })
    }

    override fun getPlaceById(
        placeId: String,
        callback: (Boolean, String, Place?) -> Unit
    ) {
        ref.child(placeId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val place = snapshot.getValue(Place::class.java)
                    if (place != null) {
                        callback(true, "Done", place)
                    } else {
                        callback(false, "Place not found", null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, null)
                }
            })
    }

    override fun searchPlaces(
        query: String,
        callback: (Boolean, String, List<Place>) -> Unit
    ) {
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val results = snapshot.children.mapNotNull {
                    it.getValue(Place::class.java)
                }.filter {
                    it.name.contains(query, ignoreCase = true) ||
                            it.location.contains(query, ignoreCase = true) ||
                            it.category.contains(query, ignoreCase = true)
                }
                callback(true, "Done", results)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, emptyList())
            }
        })
    }
}