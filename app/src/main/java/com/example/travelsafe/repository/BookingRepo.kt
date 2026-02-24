package com.example.travelsafe.repository

import com.example.travelsafe.model.Booking

interface BookingRepo {

    fun createBooking(
        booking: Booking,
        callback: (Boolean, String) -> Unit
    )

    fun getUserBookings(
        userId: String,
        callback: (Boolean, String, List<Booking>) -> Unit
    )

    fun getAllBookings(
        callback: (Boolean, String, List<Booking>) -> Unit
    )

    fun updateBookingStatus(
        bookingId: String,
        status: String,
        callback: (Boolean, String) -> Unit
    )

    fun cancelBooking(
        bookingId: String,
        callback: (Boolean, String) -> Unit
    )

    fun getBookingById(
        bookingId: String,
        callback: (Boolean, String, Booking?) -> Unit
    )
}