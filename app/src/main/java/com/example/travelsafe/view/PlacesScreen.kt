package com.example.travelsafe.view

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
//DEbugged
@Composable
fun PlacesScreen(padding: PaddingValues = PaddingValues()) {
    val repo = remember { FirebasePlaceRepoImpl() }
    var places by remember { mutableStateOf(listOf<Place>()) }
    var filteredPlaces by remember { mutableStateOf(listOf<Place>()) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current

    val categories = listOf("All", "General", "Beach", "Mountain", "Culture", "Food")

    LaunchedEffect(Unit) {
        repo.getAllPlaces { success, _, list ->
            places = list
            filteredPlaces = list
            isLoading = false
        }
    }

    // Filter whenever search or category changes
    LaunchedEffect(searchQuery, selectedCategory) {
        filteredPlaces = places.filter { place ->
            val matchesSearch = searchQuery.isEmpty() ||
                    place.name.contains(searchQuery, ignoreCase = true) ||
                    place.location.contains(searchQuery, ignoreCase = true) ||
                    place.description.contains(searchQuery, ignoreCase = true)
            val matchesCategory = selectedCategory == "All" || place.category == selectedCategory
            matchesSearch && matchesCategory
        }
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
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF2D3142), Color(0xFF4A4E69))
                    )
                )
                .padding(top = 40.dp, bottom = 20.dp, start = 20.dp, end = 20.dp)
        ) {
            Column {
                Text(
                    "Explore Places",
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                )
                Text(
                    "Discover amazing destinations",
                    style = TextStyle(fontSize = 13.sp, color = Color.White.copy(alpha = 0.7f))
                )
            }
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search places, locations...") },
            leadingIcon = {
                Icon(painter = painterResource(R.drawable.baseline_search_24), contentDescription = null, tint = Color(0xFF6366F1))
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(painter = painterResource(R.drawable.baseline_explore_24), contentDescription = "Clear", tint = Color(0xFF9E9E9E))
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF6366F1),
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            singleLine = true
        )

        // Category Filter
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            items(categories) { cat ->
                val isSelected = cat == selectedCategory
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) Color(0xFF6366F1) else Color.White)
                        .clickable { selectedCategory = cat }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        cat,
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else Color(0xFF9E9E9E)
                        )
                    )
                }
            }
        }

        // Results count
        Text(
            "${filteredPlaces.size} place${if (filteredPlaces.size != 1) "s" else ""} found",
            style = TextStyle(fontSize = 13.sp, color = Color(0xFF9E9E9E), fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
        )

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF6366F1))
            }
        } else if (filteredPlaces.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_explore_24),
                        contentDescription = null,
                        tint = Color(0xFFCCCCCC),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        if (searchQuery.isEmpty()) "No places available yet"
                        else "No results for \"$searchQuery\"",
                        style = TextStyle(fontSize = 16.sp, color = Color(0xFF9E9E9E))
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredPlaces) { place ->
                    PlaceCard(
                        place = place,
                        onClick = {
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

@Composable
fun PlaceCard(place: Place, onClick: () -> Unit) {
    val categoryColor = when (place.category) {
        "Beach" -> Color(0xFF0EA5E9)
        "Mountain" -> Color(0xFF16A34A)
        "Culture" -> Color(0xFFD97706)
        "Food" -> Color(0xFFDC2626)
        else -> Color(0xFF6366F1)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                if (place.imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = place.imageUrl,
                        contentDescription = place.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                            .background(Color(0xFFEEEEFF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_explore_24),
                            contentDescription = null,
                            tint = Color(0xFF6366F1),
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                // Category badge on image
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopStart)
                        .background(categoryColor, RoundedCornerShape(20.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        place.category,
                        style = TextStyle(fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    )
                }

                // Rating badge
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopEnd)
                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        "⭐ ${place.rating}",
                        style = TextStyle(fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    )
                }
            }

            // Info
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            place.name,
                            style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D3142))
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_explore_24),
                                contentDescription = null,
                                tint = Color(0xFF9E9E9E),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                place.location,
                                style = TextStyle(fontSize = 13.sp, color = Color(0xFF9E9E9E))
                            )
                        }
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            place.price,
                            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6366F1))
                        )
                        Text(
                            "${place.reviews} reviews",
                            style = TextStyle(fontSize = 11.sp, color = Color(0xFF9E9E9E))
                        )
                    }
                }

                if (place.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        place.description.take(100) + if (place.description.length > 100) "..." else "",
                        style = TextStyle(fontSize = 13.sp, color = Color(0xFF666666), lineHeight = 18.sp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onClick,
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1))
                ) {
                    Text(
                        "Book Now",
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    )
                }
            }
        }
    }
}