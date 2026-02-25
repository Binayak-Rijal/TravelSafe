package com.example.travelsafe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelsafe.ui.theme.TravelSafeTheme
import com.example.travelsafe.view.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class DashBoardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val fullName = intent.getStringExtra("fullName") ?: run {
            FirebaseAuth.getInstance().currentUser?.displayName ?: "User"
        }

        setContent {
            TravelSafeTheme {
                DashboardScreen(fullName = fullName)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(fullName: String = "User") {
    var selectedTab by remember { mutableStateOf(0) }
    var showSearchSheet by remember { mutableStateOf(false) }
    var showNotifications by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    // Notification screen overlay
    if (showNotifications) {
        NotificationScreen(onBackClick = { showNotifications = false })
        return
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { padding ->
        when (selectedTab) {
            0 -> HomeContent(
                fullName = fullName,
                padding = padding,
                onSearchClick = { showSearchSheet = true },
                onNotificationClick = { showNotifications = true }
            )
            1 -> TripPlannerScreen()
            2 -> MyBookingsScreen(padding = padding)
            3 -> BookmarkScreen()
            4 -> ProfileScreen()
        }
    }

    // Search Bottom Sheet
    if (showSearchSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSearchSheet = false },
            sheetState = sheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            SearchBottomSheetContent(
                onClose = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        showSearchSheet = false
                    }
                }
            )
        }
    }
}

// ── Search Bottom Sheet ────────────────────────────────────────────────────────
// Searches Firebase places in real-time

@Composable
fun SearchBottomSheetContent(onClose: () -> Unit = {}) {
    var query by remember { mutableStateOf("") }
    val repo = remember { com.example.travelsafe.repository.FirebasePlaceRepoImpl() }
    var allPlaces by remember { mutableStateOf(listOf<com.example.travelsafe.model.Place>()) }
    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(Unit) {
        repo.getAllPlaces { _, _, list -> allPlaces = list }
    }

    val filtered = if (query.isEmpty()) allPlaces
    else allPlaces.filter {
        it.name.contains(query, ignoreCase = true) ||
                it.location.contains(query, ignoreCase = true) ||
                it.category.contains(query, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp)
    ) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color(0xFFE0E0E0))
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Search Places",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D3142))
            )
            IconButton(onClick = onClose) {
                Icon(
                    painter = painterResource(R.drawable.baseline_explore_24),
                    contentDescription = "Close",
                    tint = Color(0xFF9E9E9E)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search places, locations...", style = TextStyle(color = Color(0xFF9E9E9E))) },
            leadingIcon = {
                Icon(painter = painterResource(R.drawable.baseline_search_24), contentDescription = null, tint = Color(0xFF6366F1))
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { query = "" }) {
                        Icon(painter = painterResource(R.drawable.baseline_explore_24), contentDescription = "Clear", tint = Color(0xFF9E9E9E))
                    }
                }
            },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF6366F1),
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedContainerColor = Color(0xFFFAFAFA),
                unfocusedContainerColor = Color(0xFFFAFAFA)
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            if (query.isEmpty()) "All Places (${allPlaces.size})" else "${filtered.size} results for \"$query\"",
            style = TextStyle(fontSize = 13.sp, color = Color(0xFF9E9E9E), fontWeight = FontWeight.Medium)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.heightIn(max = 300.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filtered) { place ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    onClick = {
                        // Open BookingActivity from search result
                        val intent = android.content.Intent(context, BookingActivity::class.java).apply {
                            putExtra("placeId", place.placeId)
                            putExtra("placeName", place.name)
                            putExtra("placeLocation", place.location)
                            putExtra("placePrice", place.price)
                            putExtra("placeImageUrl", place.imageUrl)
                            putExtra("placeDescription", place.description)
                            putExtra("placeRating", place.rating)
                        }
                        context.startActivity(intent)
                        onClose()
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_explore_24),
                                contentDescription = null,
                                tint = Color(0xFF6366F1),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(place.name, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2D3142)))
                                Text(place.location, style = TextStyle(fontSize = 12.sp, color = Color(0xFF9E9E9E)))
                            }
                        }
                        Text(place.price, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6366F1)))
                    }
                }
            }

            if (filtered.isEmpty() && query.isNotEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                        Text("No places found for \"$query\"", style = TextStyle(fontSize = 14.sp, color = Color(0xFF9E9E9E)))
                    }
                }
            }
        }
    }
}

// ── Home Content ───────────────────────────────────────────────────────────────

@Composable
fun HomeContent(
    fullName: String,
    padding: PaddingValues,
    onSearchClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(padding)
            .verticalScroll(scrollState)
    ) {
        HeaderSection(
            fullName = fullName,
            onSearchClick = onSearchClick,
            onNotificationClick = onNotificationClick
        )
        Spacer(modifier = Modifier.height(20.dp))
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
        TravelCategoriesSection()
        Spacer(modifier = Modifier.height(30.dp))
        SchedulesSection()
        Spacer(modifier = Modifier.height(30.dp))
        // ✅ RecommendationSection now loads from Firebase
        RecommendationSection()
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun HeaderSection(
    fullName: String,
    onSearchClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8E8E8)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_person_24),
                    contentDescription = "Profile",
                    tint = Color(0xFF9E9E9E),
                    modifier = Modifier.size(30.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                fullName,
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2D3142))
            )
        }
        Row {
            IconButton(onClick = onNotificationClick) {
                Icon(
                    painter = painterResource(R.drawable.baseline_explore_24),
                    contentDescription = "Notifications",
                    tint = Color(0xFF2D3142)
                )
            }
            IconButton(onClick = onSearchClick) {
                Icon(
                    painter = painterResource(R.drawable.baseline_search_24),
                    contentDescription = "Search",
                    tint = Color(0xFF2D3142)
                )
            }
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
            TravelCategoryCard(category = category)
        }
    }
}

@Composable
fun TravelCategoryCard(category: TravelCategory, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = category.color),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
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
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.White)
            )
        }
    }
}

// ✅ Updated bottom nav — 5 tabs including "Bookings"
@Composable
fun BottomNavigationBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar(containerColor = Color.White, contentColor = Color(0xFF6366F1)) {
        val navItems = listOf(
            Triple(R.drawable.baseline_home_24, "Home", 0),
            Triple(R.drawable.baseline_flight_24, "Trips", 1),
            Triple(R.drawable.baseline_bookmark_24, "Bookings", 2),
            Triple(R.drawable.baseline_bookmark_24, "Saved", 3),
            Triple(R.drawable.baseline_person_24, "Profile", 4)
        )
        navItems.forEach { (icon, label, index) ->
            NavigationBarItem(
                icon = { Icon(painter = painterResource(icon), contentDescription = label) },
                label = { Text(label, style = TextStyle(fontSize = 10.sp)) },
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
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
}

data class TravelCategory(val name: String, val color: Color, val icon: Int)

@Preview(showBackground = true)
@Composable
fun PreviewDashboard() {
    TravelSafeTheme { DashboardScreen() }
}