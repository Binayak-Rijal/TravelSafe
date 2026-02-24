package com.example.travelsafe.view.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelsafe.R
import com.example.travelsafe.viewmodel.AdminViewModel

@Composable
fun AdminStatsScreen(
    adminViewModel: AdminViewModel,
    padding: PaddingValues
) {
    val places = adminViewModel.places
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        adminViewModel.loadAllPlaces()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0FF))
            .padding(padding)
            .verticalScroll(scrollState)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E1B4B))
                .padding(top = 40.dp, bottom = 20.dp, start = 20.dp, end = 20.dp)
        ) {
            Column {
                Text(
                    "Admin Dashboard",
                    style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                )
                Text(
                    "TravelSafe Management Panel",
                    style = TextStyle(fontSize = 13.sp, color = Color.White.copy(alpha = 0.7f))
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Stats Cards Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AdminStatCard(
                modifier = Modifier.weight(1f),
                title = "Total Places",
                value = "${places.size}",
                icon = R.drawable.baseline_explore_24,
                color = Color(0xFF4338CA)
            )
            AdminStatCard(
                modifier = Modifier.weight(1f),
                title = "Categories",
                value = "${places.map { it.category }.distinct().size}",
                icon = R.drawable.baseline_bookmark_24,
                color = Color(0xFF059669)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AdminStatCard(
                modifier = Modifier.weight(1f),
                title = "Avg Rating",
                value = if (places.isEmpty()) "N/A"
                else String.format("%.1f", places.map { it.rating }.average()),
                icon = R.drawable.baseline_flight_24,
                color = Color(0xFFD97706)
            )
            AdminStatCard(
                modifier = Modifier.weight(1f),
                title = "Total Reviews",
                value = "${places.sumOf { it.reviews }}",
                icon = R.drawable.baseline_person_24,
                color = Color(0xFFDC2626)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Recent Places
        Text(
            "All Places",
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E1B4B)),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (places.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_explore_24),
                        contentDescription = null,
                        tint = Color(0xFFCCCCCC),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No places added yet", style = TextStyle(fontSize = 14.sp, color = Color(0xFF9E9E9E)))
                }
            }
        } else {
            places.forEach { place ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                place.name,
                                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E1B4B))
                            )
                            Text(
                                place.location,
                                style = TextStyle(fontSize = 12.sp, color = Color(0xFF9E9E9E))
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                place.price,
                                style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4338CA))
                            )
                            Text(
                                "⭐ ${place.rating}",
                                style = TextStyle(fontSize = 12.sp, color = Color(0xFF9E9E9E))
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun AdminStatCard(
    modifier: Modifier,
    title: String,
    value: String,
    icon: Int,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = color))
            Text(title, style = TextStyle(fontSize = 12.sp, color = Color(0xFF9E9E9E)))
        }
    }
}