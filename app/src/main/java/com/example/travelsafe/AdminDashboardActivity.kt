package com.example.travelsafe

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelsafe.ui.theme.TravelSafeTheme
import com.example.travelsafe.view.admin.AdminBookingsScreen
import com.example.travelsafe.view.admin.AdminManagePlacesScreen
import com.example.travelsafe.view.admin.AdminStatsScreen
import com.example.travelsafe.viewmodel.AdminViewModel
import com.example.travelsafe.viewmodel.BookingViewModel

class AdminDashboardActivity : ComponentActivity() {

    private val adminViewModel: AdminViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TravelSafeTheme {
                AdminDashboardScreen(
                    adminViewModel = adminViewModel,
                    onLogout = {
                        adminViewModel.adminLogout()
                        startActivity(Intent(this, AdminLoginActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        })
                    }
                )
            }
        }
    }
}

@Composable
fun AdminDashboardScreen(
    adminViewModel: AdminViewModel = AdminViewModel(),
    onLogout: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) }
    val bookingViewModel: BookingViewModel = viewModel()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF1E1B4B),
                contentColor = Color.White
            ) {
                listOf(
                    Triple(R.drawable.baseline_home_24, "Dashboard", 0),
                    Triple(R.drawable.baseline_explore_24, "Places", 1),
                    Triple(R.drawable.baseline_flight_24, "Bookings", 2),
                    Triple(R.drawable.baseline_person_24, "Account", 3)
                ).forEach { (icon, label, index) ->
                    NavigationBarItem(
                        icon = { Icon(painter = painterResource(icon), contentDescription = label) },
                        label = { Text(label, style = TextStyle(fontSize = 11.sp)) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = Color(0xFF818CF8),
                            unselectedIconColor = Color.White.copy(alpha = 0.5f),
                            unselectedTextColor = Color.White.copy(alpha = 0.5f),
                            indicatorColor = Color(0xFF4338CA)
                        )
                    )
                }
            }
        }
    ) { padding ->
        when (selectedTab) {
            0 -> AdminStatsScreen(adminViewModel = adminViewModel, padding = padding)
            1 -> AdminManagePlacesScreen(adminViewModel = adminViewModel, padding = padding)
            2 -> AdminBookingsScreen(padding = padding, bookingViewModel = bookingViewModel)
            3 -> AdminAccountScreen(adminViewModel = adminViewModel, onLogout = onLogout, padding = padding)
        }
    }
}

@Composable
fun AdminAccountScreen(
    adminViewModel: AdminViewModel,
    onLogout: () -> Unit,
    padding: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(padding)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E1B4B))
                .padding(top = 40.dp, bottom = 20.dp, start = 20.dp, end = 20.dp)
        ) {
            Text("Admin Account", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White))
        }

        Spacer(modifier = Modifier.height(30.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4338CA)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_person_24),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(45.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Administrator", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E1B4B)))
            Text("admin@gmail.com", style = TextStyle(fontSize = 14.sp, color = Color(0xFF9E9E9E)))
        }

        Spacer(modifier = Modifier.height(30.dp))

        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Admin Info", style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF9E9E9E)))
                Spacer(modifier = Modifier.height(12.dp))
                AdminInfoRow("Role", "Super Admin")
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFF5F5F5))
                AdminInfoRow("App", "TravelSafe v1.0.0")
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFF5F5F5))
                AdminInfoRow("Database", "Firebase Realtime DB")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252))
        ) {
            Text("Logout", style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White))
        }
    }
}

@Composable
fun AdminInfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = TextStyle(fontSize = 14.sp, color = Color(0xFF9E9E9E)))
        Text(value, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1E1B4B)))
    }
}