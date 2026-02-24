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
import coil.compose.AsyncImage
import com.example.travelsafe.R
import com.example.travelsafe.model.FlightSchedule

@Composable
fun SchedulesSection() {
    val schedules = listOf(
        FlightSchedule(
            airline = "Delta Airlines",
            airlineLogoUrl = "https://res.cloudinary.com/YOUR_CLOUD_NAME/image/upload/delta_logo.png",
            origin = "New York",
            originCode = "JFK",
            destination = "Los Angeles",
            destinationCode = "LAX",
            duration = "2h 45m",
            price = "$170.6/per",
            rating = 4
        ),
        FlightSchedule(
            airline = "Emirates",
            airlineLogoUrl = "https://res.cloudinary.com/YOUR_CLOUD_NAME/image/upload/emirates_logo.png",
            origin = "Dubai",
            originCode = "DXB",
            destination = "London",
            destinationCode = "LHR",
            duration = "7h 20m",
            price = "$540.0/per",
            rating = 5
        ),
        FlightSchedule(
            airline = "Air Asia",
            airlineLogoUrl = "https://res.cloudinary.com/YOUR_CLOUD_NAME/image/upload/airasia_logo.png",
            origin = "Bangkok",
            originCode = "BKK",
            destination = "Singapore",
            destinationCode = "SIN",
            duration = "2h 10m",
            price = "$95.0/per",
            rating = 4
        ),
        FlightSchedule(
            airline = "Qatar Airways",
            airlineLogoUrl = "https://res.cloudinary.com/YOUR_CLOUD_NAME/image/upload/qatar_logo.png",
            origin = "Doha",
            originCode = "DOH",
            destination = "Paris",
            destinationCode = "CDG",
            duration = "6h 55m",
            price = "$480.0/per",
            rating = 5
        ),
        FlightSchedule(
            airline = "Himalaya Airlines",
            airlineLogoUrl = "https://res.cloudinary.com/YOUR_CLOUD_NAME/image/upload/himalaya_logo.png",
            origin = "Kathmandu",
            originCode = "KTM",
            destination = "Pokhara",
            destinationCode = "PKR",
            duration = "0h 25m",
            price = "$45.0/per",
            rating = 3
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
                    AsyncImage(
                        model = schedule.airlineLogoUrl,
                        contentDescription = schedule.airline,
                        modifier = Modifier.size(20.dp)
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
                Column {
                    Text(schedule.origin, style = TextStyle(fontSize = 11.sp, color = Color(0xFF9E9E9E)))
                    Text(
                        schedule.originCode,
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D3142))
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(schedule.duration, style = TextStyle(fontSize = 11.sp, color = Color(0xFF9E9E9E)))
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF6366F1)))
                        Box(modifier = Modifier.weight(1f).height(1.dp).background(Color(0xFFCCCCCC)))
                        Icon(
                            painter = painterResource(R.drawable.baseline_flight_24),
                            contentDescription = null,
                            tint = Color(0xFF6366F1),
                            modifier = Modifier.size(18.dp)
                        )
                        Box(modifier = Modifier.weight(1f).height(1.dp).background(Color(0xFFCCCCCC)))
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFFFD700)))
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(schedule.destination, style = TextStyle(fontSize = 11.sp, color = Color(0xFF9E9E9E)))
                    Text(
                        schedule.destinationCode,
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D3142))
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) { index ->
                        Icon(
                            painter = painterResource(R.drawable.baseline_bookmark_24),
                            contentDescription = null,
                            tint = if (index < schedule.rating) Color(0xFFFFD700) else Color(0xFFE0E0E0),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
                Text(
                    schedule.price,
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6366F1))
                )
            }
        }
    }
}