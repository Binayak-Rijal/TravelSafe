package com.example.travelsafe.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelsafe.R

data class FlightSchedule(
    val airline: String,
    val airlineLogo: Int,
    val origin: String,
    val originCode: String,
    val destination: String,
    val destinationCode: String,
    val duration: String,
    val price: String,
    val rating: Int
)

@Composable
fun FlightSchedulesSection() {
    val schedules = listOf(
        FlightSchedule(
            airline = "Delta Airlines",
            airlineLogo = R.drawable.baseline_flight_24,
            origin = "NewYork",
            originCode = "JFK",
            destination = "LosAngles",
            destinationCode = "LAX",
            duration = "2h 45m",
            price = "$170.6/per",
            rating = 4
        )
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Upcoming Schedules",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3142)
            ),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))

        schedules.forEach { schedule ->
            FlightScheduleCard(schedule)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun FlightScheduleCard(schedule: FlightSchedule) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Airline Row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE8F0FE)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(schedule.airlineLogo),
                        contentDescription = schedule.airline,
                        tint = Color(0xFF6366F1),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    schedule.airline,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2D3142)
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Route Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Origin
                Column {
                    Text(
                        schedule.origin,
                        style = TextStyle(fontSize = 11.sp, color = Color(0xFF9E9E9E))
                    )
                    Text(
                        schedule.originCode,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3142)
                        )
                    )
                }

                // Duration + line
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        schedule.duration,
                        style = TextStyle(fontSize = 11.sp, color = Color(0xFF9E9E9E))
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF6366F1))
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(1.dp)
                                .background(Color(0xFFCCCCCC))
                        )
                        Icon(
                            painter = painterResource(R.drawable.baseline_flight_24),
                            contentDescription = null,
                            tint = Color(0xFF6366F1),
                            modifier = Modifier.size(18.dp)
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(1.dp)
                                .background(Color(0xFFCCCCCC))
                        )
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFD700))
                        )
                    }
                }

                // Destination
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        schedule.destination,
                        style = TextStyle(fontSize = 11.sp, color = Color(0xFF9E9E9E))
                    )
                    Text(
                        schedule.destinationCode,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3142)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Rating + Price Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_bookmark_24),
                        contentDescription = "Rating",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "${schedule.rating}",
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF2D3142)
                        )
                    )
                }

                Text(
                    schedule.price,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6366F1)
                    )
                )
            }
        }
    }
}