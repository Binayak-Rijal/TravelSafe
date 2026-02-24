package com.example.travelsafe.repository

import com.example.travelsafe.model.Place
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebasePlaceRepoImpl {

    private val ref = FirebaseDatabase.getInstance().getReference("places")

    fun getAllPlaces(callback: (Boolean, String, List<Place>) -> Unit) {
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

    fun searchPlaces(query: String, callback: (Boolean, String, List<Place>) -> Unit) {
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val results = snapshot.children.mapNotNull {
                    it.getValue(Place::class.java)
                }.filter {
                    it.name.contains(query, ignoreCase = true) ||
                            it.location.contains(query, ignoreCase = true) ||
                            it.description.contains(query, ignoreCase = true) ||
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