package com.example.travelsafe.view

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.travelsafe.BookingActivity
import com.example.travelsafe.R
import com.example.travelsafe.model.Place
import com.example.travelsafe.repository.FirebasePlaceRepoImpl

// ── Recommendation Section ────────────────────────────────────────────────────
// Loads places added by admin from Firebase Realtime Database
// Each card has a "Book Now" button that opens BookingActivity

@Composable
fun RecommendationSection() {
    val repo = remember { FirebasePlaceRepoImpl() }
    var places by remember { mutableStateOf(listOf<Place>()) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current

    // Load Firebase places once
    LaunchedEffect(Unit) {
        repo.getAllPlaces { _, _, list ->
            places = list
            isLoading = false
        }
    }

    Column {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Recommended",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3142)
                )
            )
            if (places.isNotEmpty()) {
                Text(
                    "${places.size} places",
                    style = TextStyle(fontSize = 13.sp, color = Color(0xFF9E9E9E))
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF6366F1))
                }
            }

            places.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(horizontal = 20.dp)
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_explore_24),
                            contentDescription = null,
                            tint = Color(0xFFCCCCCC),
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "No places yet",
                            style = TextStyle(fontSize = 14.sp, color = Color(0xFF9E9E9E))
                        )
                    }
                }
            }

            else -> {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(places) { place ->
                        RecommendedPlaceCard(
                            place = place,
                            onBookClick = {
                                val intent = Intent(context, BookingActivity::class.java).apply {
                                    putExtra("placeId", place.placeId)
                                    putExtra("placeName", place.name)
                                    putExtra("placeLocation", place.location)
                                    putExtra("placePrice", place.price)
                                    putExtra("placeImageUrl", place.imageUrl)
                                    putExtra("placeDescription", place.description)
                                    putExtra("placeRating", place.rating)
                                }
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecommendedPlaceCard(place: Place, onBookClick: () -> Unit) {
    val categoryColor = when (place.category) {
        "Beach" -> Color(0xFF0EA5E9)
        "Mountain" -> Color(0xFF16A34A)
        "Culture" -> Color(0xFFD97706)
        "Food" -> Color(0xFFDC2626)
        else -> Color(0xFF6366F1)
    }

    Card(
        modifier = Modifier
            .width(200.dp)
            .wrapContentHeight(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column {
            // Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                if (place.imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = place.imageUrl,
                        contentDescription = place.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color(0xFF6366F1), Color(0xFF8B5CF6))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_explore_24),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                // Rating badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.55f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Text(
                        "⭐ ${place.rating}",
                        style = TextStyle(fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    )
                }

                // Category badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .background(categoryColor, RoundedCornerShape(20.dp))
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Text(
                        place.category,
                        style = TextStyle(fontSize = 9.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    )
                }
            }

            // Info
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    place.name,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3142)
                    ),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(3.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_explore_24),
                        contentDescription = null,
                        tint = Color(0xFF9E9E9E),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        place.location,
                        style = TextStyle(fontSize = 11.sp, color = Color(0xFF9E9E9E)),
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        place.price,
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6366F1)
                        )
                    )
                    Text(
                        "${place.reviews} reviews",
                        style = TextStyle(fontSize = 9.sp, color = Color(0xFFBBBBBB))
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Book Now Button
                Button(
                    onClick = onBookClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        "Book Now",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}