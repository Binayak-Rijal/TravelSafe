package com.example.travelsafe.repository

import com.example.travelsafe.model.Booking
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class BookingRepoImpl : BookingRepo {

    private val ref = FirebaseDatabase.getInstance().getReference("bookings")

    override fun createBooking(
        booking: Booking,
        callback: (Boolean, String) -> Unit
    ) {
        val bookingId = ref.push().key ?: run {
            callback(false, "Failed to generate booking ID")
            return
        }

        val timestamp = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
            .format(Date())

        val newBooking = booking.copy(
            bookingId = bookingId,
            bookedAt = timestamp
        )

        ref.child(bookingId).setValue(newBooking)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Booking confirmed! ID: $bookingId")
                } else {
                    callback(false, task.exception?.message ?: "Booking failed")
                }
            }
    }

    override fun getUserBookings(
        userId: String,
        callback: (Boolean, String, List<Booking>) -> Unit
    ) {
        ref.orderByChild("userId").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val bookings = snapshot.children.mapNotNull {
                        it.getValue(Booking::class.java)
                    }.sortedByDescending { it.bookedAt }
                    callback(true, "Done", bookings)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, emptyList())
                }
            })
    }

    override fun getAllBookings(
        callback: (Boolean, String, List<Booking>) -> Unit
    ) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bookings = snapshot.children.mapNotNull {
                    it.getValue(Booking::class.java)
                }.sortedByDescending { it.bookedAt }
                callback(true, "Done", bookings)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, emptyList())
            }
        })
    }

    override fun updateBookingStatus(
        bookingId: String,
        status: String,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(bookingId).child("status").setValue(status)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Status updated to $status")
                } else {
                    callback(false, task.exception?.message ?: "Update failed")
                }
            }
    }

    override fun cancelBooking(
        bookingId: String,
        callback: (Boolean, String) -> Unit
    ) {
        updateBookingStatus(bookingId, "Cancelled", callback)
    }

    override fun getBookingById(
        bookingId: String,
        callback: (Boolean, String, Booking?) -> Unit
    ) {
        ref.child(bookingId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val booking = snapshot.getValue(Booking::class.java)
                    if (booking != null) callback(true, "Done", booking)
                    else callback(false, "Booking not found", null)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, null)
                }
            })
    }
}