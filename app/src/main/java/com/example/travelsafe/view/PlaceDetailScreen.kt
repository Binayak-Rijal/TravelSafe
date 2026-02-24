package com.example.travelsafe.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.travelsafe.R
import com.example.travelsafe.model.RecommendedPlace
import com.example.travelsafe.utils.BookmarkManager

@Composable
fun PlaceDetailScreen(
    place: RecommendedPlace,
    onBackClick: () -> Unit = {},
    onBookNowClick: (RecommendedPlace) -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val isBookmarked = BookmarkManager.isBookmarked(place)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(scrollState)
    ) {
        // Hero Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            AsyncImage(
                model = place.imageUrl,
                contentDescription = place.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.3f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.5f)
                            )
                        )
                    )
            )

            // Back button
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(16.dp)
                    .padding(top = 24.dp)
                    .align(Alignment.TopStart)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.9f))
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_flight_24),
                    contentDescription = "Back",
                    tint = Color(0xFF2D3142),
                    modifier = Modifier.size(20.dp)
                )
            }

            // ✅ Bookmark button uses BookmarkManager
            IconButton(
                onClick = { BookmarkManager.toggle(place) },
                modifier = Modifier
                    .padding(16.dp)
                    .padding(top = 24.dp)
                    .align(Alignment.TopEnd)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.9f))
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_bookmark_24),
                    contentDescription = "Bookmark",
                    tint = if (isBookmarked) Color(0xFF6366F1) else Color(0xFF9E9E9E),
                    modifier = Modifier.size(20.dp)
                )
            }

            Text(
                text = place.name,
                style = TextStyle(
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp)
            )
        }

        Column(modifier = Modifier.padding(20.dp)) {

            // Rating + Price
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
                            tint = if (index < place.rating.toInt()) Color(0xFFFFD700) else Color(0xFFE0E0E0),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "${place.rating} (${place.reviews} reviews)",
                        style = TextStyle(fontSize = 13.sp, color = Color(0xFF9E9E9E))
                    )
                }
                Text(
                    place.price,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6366F1)
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Location
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.baseline_explore_24),
                    contentDescription = null,
                    tint = Color(0xFF6366F1),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    place.location,
                    style = TextStyle(fontSize = 13.sp, color = Color(0xFF9E9E9E))
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // About
            Text(
                "About",
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D3142))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                place.description,
                style = TextStyle(fontSize = 14.sp, color = Color(0xFF666666), lineHeight = 22.sp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Highlights
            if (place.highlights.isNotEmpty()) {
                Text(
                    "Highlights",
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D3142))
                )
                Spacer(modifier = Modifier.height(12.dp))
                place.highlights.forEach { highlight ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF6366F1))
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            highlight,
                            style = TextStyle(fontSize = 14.sp, color = Color(0xFF444444))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Bookmark status banner
            if (isBookmarked) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF2FF))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_bookmark_24),
                            contentDescription = null,
                            tint = Color(0xFF6366F1),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Saved to your bookmarks",
                            style = TextStyle(
                                fontSize = 13.sp,
                                color = Color(0xFF6366F1),
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Book Now Button
            Button(
                onClick = { onBookNowClick(place) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1))
            ) {
                Text(
                    "Book Now",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}