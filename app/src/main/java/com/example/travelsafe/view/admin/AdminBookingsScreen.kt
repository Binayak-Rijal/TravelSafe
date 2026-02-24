package com.example.travelsafe.view.admin

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelsafe.R
import com.example.travelsafe.model.Booking
import com.example.travelsafe.viewmodel.BookingViewModel

@Composable
fun AdminBookingsScreen(
    padding: PaddingValues = PaddingValues(),
    bookingViewModel: BookingViewModel = viewModel()
) {

    val bookings = bookingViewModel.allBookings
    val context = LocalContext.current

    var selectedFilter by remember { mutableStateOf("All") }

    val filters = listOf("All", "Pending", "Confirmed", "Cancelled")

    LaunchedEffect(Unit) {
        bookingViewModel.loadAllBookings()
    }

    val filtered =
        if (selectedFilter == "All")
            bookings.toList()
        else
            bookings.filter { it.status == selectedFilter }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0FF))
            .padding(padding)
    ) {

        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E1B4B))
                .padding(top = 40.dp, bottom = 16.dp, start = 20.dp, end = 20.dp)
        ) {

            Column {

                Text(
                    "Manage Bookings",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )

                Text(
                    "${bookings.size} total bookings",
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                )

            }

        }


        // Stats Row

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            val pending = bookings.count { it.status == "Pending" }

            val confirmed = bookings.count { it.status == "Confirmed" }

            val cancelled = bookings.count { it.status == "Cancelled" }


            AdminBookingStat(
                modifier = Modifier.weight(1f),
                label = "Pending",
                value = "$pending",
                color = Color(0xFFD97706)
            )

            AdminBookingStat(
                modifier = Modifier.weight(1f),
                label = "Confirmed",
                value = "$confirmed",
                color = Color(0xFF22C55E)
            )

            AdminBookingStat(
                modifier = Modifier.weight(1f),
                label = "Cancelled",
                value = "$cancelled",
                color = Color(0xFFFF5252)
            )

        }



        // Filter Tabs

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(Color.White, RoundedCornerShape(12.dp))
                .padding(4.dp),

            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            filters.forEach { filter ->

                val isSelected = filter == selectedFilter

                Box(

                    modifier = Modifier
                        .weight(1f)
                        .background(
                            if (isSelected)
                                Color(0xFF4338CA)
                            else
                                Color.Transparent,

                            RoundedCornerShape(10.dp)
                        )

                        .clickable(
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            indication = null
                        ) {
                            selectedFilter = filter
                        }

                        .padding(vertical = 8.dp),

                    contentAlignment = Alignment.Center

                ) {

                    Text(
                        filter,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight =
                                if (isSelected)
                                    FontWeight.Bold
                                else
                                    FontWeight.Normal,

                            color =
                                if (isSelected)
                                    Color.White
                                else
                                    Color(0xFF9E9E9E)
                        )
                    )

                }

            }

        }


        Spacer(modifier = Modifier.height(8.dp))



        if (filtered.isEmpty()) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        painter = painterResource(R.drawable.baseline_flight_24),
                        contentDescription = null,
                        tint = Color(0xFFCCCCCC),
                        modifier = Modifier.size(56.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        "No $selectedFilter bookings",
                        style = TextStyle(
                            fontSize = 15.sp,
                            color = Color(0xFF9E9E9E)
                        )
                    )

                }

            }

        }

        else {

            LazyColumn(

                contentPadding = PaddingValues(16.dp),

                verticalArrangement = Arrangement.spacedBy(10.dp)

            ) {

                items(filtered) { booking ->

                    AdminBookingCard(

                        booking = booking,

                        onConfirm = {

                            bookingViewModel.updateBookingStatus(
                                booking.bookingId,
                                "Confirmed"
                            ) { success, message ->

                                Toast.makeText(
                                    context,
                                    message,
                                    Toast.LENGTH_SHORT
                                ).show()

                            }

                        },


                        onCancel = {

                            bookingViewModel.updateBookingStatus(
                                booking.bookingId,
                                "Cancelled"
                            ) { success, message ->

                                Toast.makeText(
                                    context,
                                    message,
                                    Toast.LENGTH_SHORT
                                ).show()

                            }

                        }

                    )

                }

            }

        }

    }

}



@Composable
fun AdminBookingStat(
    modifier: Modifier,
    label: String,
    value: String,
    color: Color
) {

    Card(

        modifier = modifier,

        shape = RoundedCornerShape(12.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )

    ) {

        Column(

            modifier = Modifier.padding(12.dp),

            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Text(
                value,
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            )


            Text(
                label,
                style = TextStyle(
                    fontSize = 11.sp,
                    color = Color(0xFF9E9E9E)
                )
            )

        }

    }

}




@Composable
fun AdminBookingCard(

    booking: Booking,

    onConfirm: () -> Unit,

    onCancel: () -> Unit

) {

    val statusColor = when (booking.status) {

        "Confirmed" -> Color(0xFF22C55E)

        "Cancelled" -> Color(0xFFFF5252)

        else -> Color(0xFFD97706)

    }



    Card(

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(16.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )

    ) {

        Column(

            modifier = Modifier.padding(16.dp)

        ) {

            Row(

                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceBetween,

                verticalAlignment = Alignment.Top

            ) {

                Column(

                    modifier = Modifier.weight(1f)

                ) {

                    Text(
                        booking.placeName,
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E1B4B)
                        )
                    )


                    Text(
                        booking.placeLocation,
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = Color(0xFF9E9E9E)
                        )
                    )

                }



                Box(

                    modifier = Modifier
                        .background(
                            statusColor.copy(alpha = 0.1f),
                            RoundedCornerShape(20.dp)
                        )

                        .padding(
                            horizontal = 10.dp,
                            vertical = 4.dp
                        )

                ) {

                    Text(

                        booking.status,

                        style = TextStyle(

                            fontSize = 11.sp,

                            fontWeight = FontWeight.Bold,

                            color = statusColor

                        )

                    )

                }

            }


            Spacer(modifier = Modifier.height(10.dp))


            HorizontalDivider(
                color = Color(0xFFF5F5F5)
            )


            Spacer(modifier = Modifier.height(10.dp))



            Text(
                "👤 ${booking.userName} · ${booking.userEmail}",
                style = TextStyle(
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                )
            )



            Spacer(modifier = Modifier.height(8.dp))



            Row(

                modifier = Modifier.fillMaxWidth()

            ) {

                Column(modifier = Modifier.weight(1f)) {

                    Text(
                        "Check-in",
                        style = TextStyle(
                            fontSize = 11.sp,
                            color = Color(0xFF9E9E9E)
                        )
                    )

                    Text(
                        booking.checkInDate,
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1E1B4B)
                        )
                    )

                }



                Column(modifier = Modifier.weight(1f)) {

                    Text(
                        "Check-out",
                        style = TextStyle(
                            fontSize = 11.sp,
                            color = Color(0xFF9E9E9E)
                        )
                    )

                    Text(
                        booking.checkOutDate,
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1E1B4B)
                        )
                    )

                }



                Column(modifier = Modifier.weight(1f)) {

                    Text(
                        "Guests",
                        style = TextStyle(
                            fontSize = 11.sp,
                            color = Color(0xFF9E9E9E)
                        )
                    )

                    Text(
                        "${booking.guests}",
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1E1B4B)
                        )
                    )

                }

            }



            if (booking.specialRequests.isNotEmpty()) {

                Spacer(modifier = Modifier.height(8.dp))


                Text(
                    "📝 ${booking.specialRequests}",
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = Color(0xFF666666)
                    )
                )

            }




            if (booking.status == "Pending") {

                Spacer(modifier = Modifier.height(12.dp))



                Row(

                    modifier = Modifier.fillMaxWidth(),

                    horizontalArrangement = Arrangement.spacedBy(8.dp)

                ) {

                    OutlinedButton(

                        onClick = onCancel,

                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp),

                        shape = RoundedCornerShape(10.dp),

                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFFF5252)
                        )

                    ) {

                        Text(
                            "Reject"
                        )

                    }



                    Button(

                        onClick = onConfirm,

                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp),

                        shape = RoundedCornerShape(10.dp),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF22C55E)
                        )

                    ) {

                        Text(
                            "Confirm",
                            color = Color.White
                        )

                    }

                }

            }



            if (booking.bookedAt.isNotEmpty()) {

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    "Booked: ${booking.bookedAt}",
                    style = TextStyle(
                        fontSize = 10.sp,
                        color = Color(0xFFBBBBBB)
                    )
                )

            }

        }

    }

}