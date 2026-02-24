package com.example.travelsafe.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelsafe.R
import com.example.travelsafe.model.Booking
import com.example.travelsafe.viewmodel.BookingViewModel

@Composable
fun MyBookingsScreen(
    padding: PaddingValues = PaddingValues(),
    bookingViewModel: BookingViewModel = viewModel()
) {
    val bookings = bookingViewModel.userBookings
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        bookingViewModel.loadUserBookings()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(padding)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2D3142))
                .padding(top = 40.dp, bottom = 20.dp, start = 20.dp, end = 20.dp)
        ) {
            Column {
                Text(
                    "My Bookings",
                    style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                )
                Text(
                    "${bookings.size} booking${if (bookings.size != 1) "s" else ""}",
                    style = TextStyle(fontSize = 13.sp, color = Color.White.copy(alpha = 0.7f))
                )
            }
        }

        if (bookings.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_flight_24),
                        contentDescription = null,
                        tint = Color(0xFFCCCCCC),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("No bookings yet", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9E9E9E)))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Book a place to see it here", style = TextStyle(fontSize = 14.sp, color = Color(0xFF9E9E9E)))
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(bookings) { booking ->
                    UserBookingCard(
                        booking = booking,
                        onCancel = {
                            bookingViewModel.cancelBooking(booking.bookingId) { success, message ->
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun UserBookingCard(booking: Booking, onCancel: () -> Unit) {
    val statusColor = when (booking.status) {
        "Confirmed" -> Color(0xFF22C55E)
        "Cancelled" -> Color(0xFFFF5252)
        else -> Color(0xFFD97706)
    }

    var showCancelDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top row: place name + status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        booking.placeName,
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D3142))
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_explore_24),
                            contentDescription = null,
                            tint = Color(0xFF9E9E9E),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(booking.placeLocation, style = TextStyle(fontSize = 12.sp, color = Color(0xFF9E9E9E)))
                    }
                }
                Box(
                    modifier = Modifier
                        .background(statusColor.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        booking.status,
                        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = statusColor)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF5F5F5))
            Spacer(modifier = Modifier.height(12.dp))

            // Booking details grid
            Row(modifier = Modifier.fillMaxWidth()) {
                BookingDetailItem(modifier = Modifier.weight(1f), label = "Check-in", value = booking.checkInDate)
                BookingDetailItem(modifier = Modifier.weight(1f), label = "Check-out", value = booking.checkOutDate)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                BookingDetailItem(modifier = Modifier.weight(1f), label = "Guests", value = "${booking.guests}")
                BookingDetailItem(modifier = Modifier.weight(1f), label = "Total Price", value = booking.totalPrice)
            }

            if (booking.bookedAt.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Booked on: ${booking.bookedAt}",
                    style = TextStyle(fontSize = 11.sp, color = Color(0xFFBBBBBB))
                )
            }

            // Cancel button (only if Pending)
            if (booking.status == "Pending") {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { showCancelDialog = true },
                    modifier = Modifier.fillMaxWidth().height(40.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF5252)),
                    border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
                ) {
                    Text("Cancel Booking", style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold))
                }
            }
        }
    }

    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            title = { Text("Cancel Booking?", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)) },
            text = { Text("Are you sure you want to cancel your booking for ${booking.placeName}?", style = TextStyle(fontSize = 14.sp, color = Color(0xFF666666))) },
            confirmButton = {
                Button(
                    onClick = { showCancelDialog = false; onCancel() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252)),
                    shape = RoundedCornerShape(10.dp)
                ) { Text("Yes, Cancel") }
            },
            dismissButton = {
                TextButton(onClick = { showCancelDialog = false }) { Text("Keep Booking", color = Color(0xFF6366F1)) }
            }
        )
    }
}

@Composable
fun BookingDetailItem(modifier: Modifier, label: String, value: String) {
    Column(modifier = modifier) {
        Text(label, style = TextStyle(fontSize = 11.sp, color = Color(0xFF9E9E9E)))
        Text(value.ifEmpty { "—" }, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2D3142)))
    }
}