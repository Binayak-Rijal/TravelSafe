package com.example.travelsafe.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.travelsafe.model.AppNotification
//DEbugged
@Composable
fun NotificationScreen(onBackClick: () -> Unit = {}) {
    val notifications = remember {
        mutableStateListOf(
            AppNotification(1, "Booking Confirmed!", "Your flight JFK → LAX with Delta Airlines has been confirmed.", "2 min ago", false, "booking"),
            AppNotification(2, "Special Offer 🎉", "Get 40% off on all hotel bookings this weekend. Use code TRAVEL40.", "1 hour ago", false, "promo"),
            AppNotification(3, "Trip Reminder", "Your trip to Pokhara is tomorrow. Don't forget to pack your essentials!", "3 hours ago", true, "alert"),
            AppNotification(4, "New Places Added", "Check out 5 new recommended places added in Kathmandu Valley.", "Yesterday", true, "info"),
            AppNotification(5, "Price Drop Alert 📉", "Flights from KTM to Delhi dropped by 25%. Book now!", "2 days ago", true, "promo"),
            AppNotification(6, "Welcome to TravelSafe!", "Start exploring amazing destinations and plan your perfect trip today.", "3 days ago", true, "info")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Top Bar with back
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2D3142))
                .padding(top = 40.dp, bottom = 16.dp, start = 8.dp, end = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_flight_24),
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Text(
                        "Notifications",
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    )
                }
                val unread = notifications.count { !it.isRead }
                if (unread > 0) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFF6366F1))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text("$unread new", style = TextStyle(fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold))
                    }
                }
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(notifications) { notification ->
                NotificationCard(notification = notification)
            }
        }
    }
}

@Composable
fun NotificationCard(notification: AppNotification) {
    val iconRes = when (notification.type) {
        "booking" -> R.drawable.baseline_flight_24
        "promo" -> R.drawable.baseline_bookmark_24
        "alert" -> R.drawable.baseline_explore_24
        else -> R.drawable.baseline_search_24
    }
    val iconColor = when (notification.type) {
        "booking" -> Color(0xFF6366F1)
        "promo" -> Color(0xFFFF9800)
        "alert" -> Color(0xFFFF5252)
        else -> Color(0xFF4CAF50)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (!notification.isRead) Color(0xFFF0F0FF) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(painter = painterResource(iconRes), contentDescription = null, tint = iconColor, modifier = Modifier.size(22.dp))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        notification.title,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = if (!notification.isRead) FontWeight.Bold else FontWeight.SemiBold,
                            color = Color(0xFF2D3142)
                        )
                    )
                    Text(notification.time, style = TextStyle(fontSize = 11.sp, color = Color(0xFF9E9E9E)))
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    notification.message,
                    style = TextStyle(fontSize = 13.sp, color = Color(0xFF666666), lineHeight = 18.sp)
                )
            }

            if (!notification.isRead) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF6366F1))
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}