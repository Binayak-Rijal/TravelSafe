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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelsafe.R
//DEbugged
@Composable
fun AboutScreen(onBackClick: () -> Unit = {}) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2D3142))
                .padding(top = 40.dp, bottom = 16.dp, start = 8.dp, end = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(painter = painterResource(R.drawable.baseline_flight_24), contentDescription = "Back", tint = Color.White)
                }
                Text("About & Help", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White))
            }
        }

        Column(modifier = Modifier.fillMaxWidth().padding(30.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier.size(90.dp).clip(CircleShape).background(Color(0xFF6366F1)),
                contentAlignment = Alignment.Center
            ) {
                Icon(painter = painterResource(R.drawable.baseline_flight_24), contentDescription = null, tint = Color.White, modifier = Modifier.size(50.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("TravelSafe", style = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D3142)))
            Text("Version 1.0.0", style = TextStyle(fontSize = 13.sp, color = Color(0xFF9E9E9E)))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Your smart travel companion for safe and effortless journeys.",
                style = TextStyle(fontSize = 14.sp, color = Color(0xFF666666), textAlign = TextAlign.Center, lineHeight = 20.sp)
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Features", style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D3142)))
                Spacer(modifier = Modifier.height(12.dp))
                listOf(
                    Pair(R.drawable.baseline_search_24, "Search & compare flights, hotels and buses"),
                    Pair(R.drawable.baseline_bookmark_24, "Save your favourite places"),
                    Pair(R.drawable.baseline_flight_24, "Plan and manage your trips"),
                    Pair(R.drawable.baseline_explore_24, "Discover recommended destinations"),
                    Pair(R.drawable.baseline_person_24, "Manage your profile securely")
                ).forEach { (icon, text) ->
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 6.dp)) {
                        Icon(painter = painterResource(icon), contentDescription = null, tint = Color(0xFF6366F1), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text, style = TextStyle(fontSize = 14.sp, color = Color(0xFF444444)))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Help & FAQ", style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D3142)))
                Spacer(modifier = Modifier.height(12.dp))
                listOf(
                    Pair("How do I book a trip?", "Search your destination, select a result and tap Book Now."),
                    Pair("How do I save a place?", "Open any place and tap the bookmark icon at the top right."),
                    Pair("Can I edit my profile?", "Go to Profile tab and tap the Edit Profile button."),
                    Pair("How do I plan a trip?", "Go to Trips tab and tap the + button to add a new trip.")
                ).forEachIndexed { index, (q, a) ->
                    FaqItem(question = q, answer = a)
                    if (index < 3) HorizontalDivider(color = Color(0xFFF5F5F5), modifier = Modifier.padding(vertical = 8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF2FF)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("College Project", style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6366F1)))
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Built with Kotlin & Jetpack Compose\nFirebase Authentication & Cloudinary",
                    style = TextStyle(fontSize = 13.sp, color = Color(0xFF666666), textAlign = TextAlign.Center, lineHeight = 20.sp)
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun FaqItem(question: String, answer: String) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(question, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF2D3142)), modifier = Modifier.weight(1f))
            IconButton(onClick = { expanded = !expanded }, modifier = Modifier.size(28.dp)) {
                Icon(painter = painterResource(R.drawable.baseline_explore_24), contentDescription = null, tint = Color(0xFF6366F1), modifier = Modifier.size(18.dp))
            }
        }
        if (expanded) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(answer, style = TextStyle(fontSize = 13.sp, color = Color(0xFF666666), lineHeight = 18.sp))
        }
    }
}