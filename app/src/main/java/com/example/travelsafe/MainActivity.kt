package com.example.travelsafe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelsafe.ui.theme.TravelSafeTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val fullName = currentUser?.displayName ?: "User"

        setContent {
            TravelSafeTheme {
                DashboardScreen(fullName = fullName)
            }
        }
    }
}

@Composable
fun DashboardScreen(fullName: String = "Tina Anderson") {
    var selectedTab by remember { mutableStateOf(0) }
    val scrollState = rememberScrollState()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(padding)
                .verticalScroll(scrollState)
        ) {
            // Header Section
            HeaderSection(fullName = fullName)

            Spacer(modifier = Modifier.height(20.dp))

            // Title
            Text(
                "Travel Made\nEffortless",
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3142),
                    lineHeight = 34.sp
                ),
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Travel Categories
            TravelCategoriesSection()

            Spacer(modifier = Modifier.height(30.dp))

            // Upcoming Schedules Section
            SchedulesSection()

            Spacer(modifier = Modifier.height(30.dp))

            // Recommendation Places Section
            RecommendationSection()

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun HeaderSection(fullName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Profile Image Placeholder
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8E8E8)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_visibility_24),
                    contentDescription = "Profile",
                    tint = Color(0xFF9E9E9E),
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                fullName,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2D3142)
                )
            )
        }

        IconButton(onClick = { /* Notification action */ }) {
            Icon(
                painter = painterResource(R.drawable.baseline_visibility_24),
                contentDescription = "Notifications",
                tint = Color(0xFF2D3142)
            )
        }
    }
}

@Composable
fun TravelCategoriesSection() {
    val categories = listOf(
        TravelCategory("Travel", Color(0xFF7FE7CC), R.drawable.baseline_directions_bus_filled_24),
        TravelCategory("Flights", Color(0xFFB5C7FF), R.drawable.baseline_flight_24),
        TravelCategory("Hotels", Color(0xFFC7B5FF), R.drawable.baseline_hotel_24),
        TravelCategory("Bus", Color(0xFFFFD1A3), R.drawable.baseline_directions_bus_filled_24)
    )

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories) { category ->
            TravelCategoryCard(category)
        }
    }
}

@Composable
fun TravelCategoryCard(category: TravelCategory) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = category.color
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(category.icon),
                contentDescription = category.name,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                category.name,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            )
        }
    }
}

@Composable
fun SchedulesSection() {
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

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                repeat(9) { index ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Item $index",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = Color(0xFF666666)
                            )
                        )

                        if (index < 3) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = Color(0xFF6366F1)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RecommendationSection() {
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

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                repeat(9) { index ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Item $index",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = Color(0xFF666666)
                            )
                        )

                        if (index >= 3 && index < 6) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = Color(0xFF6366F1)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color(0xFF6366F1)
    ) {
        NavigationBarItem(
            icon = { Icon(painter = painterResource(R.drawable.baseline_bookmark_24), contentDescription = "Home") },
            label = { Text("Home") },
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF6366F1),
                selectedTextColor = Color(0xFF6366F1),
                unselectedIconColor = Color(0xFF9E9E9E),
                unselectedTextColor = Color(0xFF9E9E9E),
                indicatorColor = Color(0xFFEEF2FF)
            )
        )

        NavigationBarItem(
            icon = { Icon(painter = painterResource(R.drawable.baseline_explore_24), contentDescription = "Explorer") },
            label = { Text("Explorer") },
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF6366F1),
                selectedTextColor = Color(0xFF6366F1),
                unselectedIconColor = Color(0xFF9E9E9E),
                unselectedTextColor = Color(0xFF9E9E9E),
                indicatorColor = Color(0xFFEEF2FF)
            )
        )

        NavigationBarItem(
            icon = { Icon(painter = painterResource(R.drawable.baseline_bookmark_24), contentDescription = "Bookmark") },
            label = { Text("Bookmark") },
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF6366F1),
                selectedTextColor = Color(0xFF6366F1),
                unselectedIconColor = Color(0xFF9E9E9E),
                unselectedTextColor = Color(0xFF9E9E9E),
                indicatorColor = Color(0xFFEEF2FF)
            )
        )

        NavigationBarItem(
            icon = { Icon(painter = painterResource(R.drawable.baseline_visibility_24), contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = selectedTab == 3,
            onClick = { onTabSelected(3) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF6366F1),
                selectedTextColor = Color(0xFF6366F1),
                unselectedIconColor = Color(0xFF9E9E9E),
                unselectedTextColor = Color(0xFF9E9E9E),
                indicatorColor = Color(0xFFEEF2FF)
            )
        )
    }
}

data class TravelCategory(
    val name: String,
    val color: Color,
    val icon: Int
)

@Preview(showBackground = true)
@Composable
fun PreviewDashboard() {
    TravelSafeTheme {
        DashboardScreen()
    }
}