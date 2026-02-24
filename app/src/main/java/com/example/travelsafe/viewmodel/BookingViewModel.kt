package com.example.travelsafe.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.travelsafe.model.Booking
import com.example.travelsafe.repository.BookingRepo
import com.example.travelsafe.repository.BookingRepoImpl
import com.google.firebase.auth.FirebaseAuth

class BookingViewModel : ViewModel() {

    private val repo: BookingRepo = BookingRepoImpl()
    private val auth = FirebaseAuth.getInstance()

    val userBookings = mutableStateListOf<Booking>()
    val allBookings = mutableStateListOf<Booking>()

    fun createBooking(
        booking: Booking,
        callback: (Boolean, String) -> Unit
    ) {
        val user = auth.currentUser ?: run {
            callback(false, "Not logged in")
            return
        }

        val bookingWithUser = booking.copy(
            userId = user.uid,
            userEmail = user.email ?: "",
            userName = user.displayName ?: "User"
        )

        repo.createBooking(bookingWithUser, callback)
    }

    fun loadUserBookings() {
        val userId = auth.currentUser?.uid ?: return
        repo.getUserBookings(userId) { success, _, list ->
            if (success) {
                userBookings.clear()
                userBookings.addAll(list)
            }
        }
    }

    fun loadAllBookings() {
        repo.getAllBookings { success, _, list ->
            if (success) {
                allBookings.clear()
                allBookings.addAll(list)
            }
        }
    }

    fun updateBookingStatus(
        bookingId: String,
        status: String,
        callback: (Boolean, String) -> Unit
    ) {
        repo.updateBookingStatus(bookingId, status, callback)
    }

    fun cancelBooking(
        bookingId: String,
        callback: (Boolean, String) -> Unit
    ) {
        repo.cancelBooking(bookingId, callback)
    }
}