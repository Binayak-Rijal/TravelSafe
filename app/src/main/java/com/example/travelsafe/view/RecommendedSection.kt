package com.example.travelsafe.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.travelsafe.model.RecommendedPlace

@Composable
fun RecommendationSection(
    onPlaceClick: (RecommendedPlace) -> Unit = {}
) {
    val places = listOf(
        RecommendedPlace(
            name = "Royal Coffee Shop",
            imageUrl = "https://res.cloudinary.com/YOUR_CLOUD_NAME/image/upload/royal_coffee.jpg",
            location = "Thamel, Kathmandu",
            description = "A cozy and iconic coffee shop nestled in the heart of Thamel. Famous for its artisan brews, warm ambiance, and stunning mountain views from the rooftop terrace.",
            price = "$5/cup",
            rating = 4.5f,
            reviews = 220,
            highlights = listOf(
                "Rooftop mountain view",
                "Artisan coffee & pastries",
                "Free WiFi",
                "Live acoustic music on weekends"
            )
        ),
        RecommendedPlace(
            name = "Royal Beach",
            imageUrl = "https://res.cloudinary.com/YOUR_CLOUD_NAME/image/upload/royal_beach.jpg",
            location = "Pokhara Lakeside",
            description = "A pristine lakeside beach with crystal clear waters and breathtaking views of the Annapurna mountain range. Perfect for kayaking, swimming, and sunset watching.",
            price = "$10/person",
            rating = 4.8f,
            reviews = 340,
            highlights = listOf(
                "Annapurna mountain views",
                "Kayaking & water sports",
                "Sunset viewpoint",
                "Beachside restaurants"
            )
        ),
        RecommendedPlace(
            name = "Mountain View",
            imageUrl = "https://res.cloudinary.com/YOUR_CLOUD_NAME/image/upload/mountain_view.jpg",
            location = "Nagarkot, Bhaktapur",
            description = "One of the best spots in Nepal to witness the Himalayan sunrise. The panoramic view stretches from Dhaulagiri in the west to Kanchenjunga in the east.",
            price = "$15/person",
            rating = 4.7f,
            reviews = 190,
            highlights = listOf(
                "Himalayan sunrise view",
                "360° panoramic viewpoint",
                "Hiking trails",
                "Local cultural experience"
            )
        )
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Recommendation Places",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3142)
            ),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(places) { place ->
                RecommendedPlaceCard(
                    place = place,
                    onClick = { onPlaceClick(place) }
                )
            }
        }
    }
}

@Composable
fun RecommendedPlaceCard(
    place: RecommendedPlace,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .width(160.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
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
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                        startY = 100f
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
        ) {
            Text(
                text = place.name,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            )
            if (place.location.isNotEmpty()) {
                Text(
                    text = place.location,
                    style = TextStyle(
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                )
            }
        }
    }
}