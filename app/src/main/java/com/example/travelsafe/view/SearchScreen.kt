package com.example.travelsafe.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelsafe.R
import com.example.travelsafe.viewmodel.SearchViewModel
//DEbugged
@Composable
fun SearchScreen(
    onBackClick: () -> Unit = {},
    onSearchClick: (String, String, Int) -> Unit = { _, _, _ -> },
    searchViewModel: SearchViewModel = viewModel()
) {
    val state = searchViewModel.searchState
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(scrollState)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF7FE7CC), Color(0xFF5BC8AF))
                    )
                )
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_flight_24),
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 24.dp, top = 40.dp)
            ) {
                Text(
                    "Find Your",
                    style = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
                )
                Text(
                    "Best Trip",
                    style = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
                )
            }

            Icon(
                painter = painterResource(R.drawable.baseline_directions_bus_filled_24),
                contentDescription = "Transport",
                tint = Color.White.copy(alpha = 0.9f),
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp, top = 30.dp)
            )
        }

        // Form Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                SearchFieldLabel("From")
                Spacer(modifier = Modifier.height(6.dp))
                SearchDropdownField(
                    value = state.from,
                    onValueChange = { searchViewModel.updateFrom(it) },
                    placeholder = "Select origin",
                    leadingIcon = R.drawable.baseline_explore_24
                )

                Spacer(modifier = Modifier.height(16.dp))

                SearchFieldLabel("To")
                Spacer(modifier = Modifier.height(6.dp))
                SearchDropdownField(
                    value = state.to,
                    onValueChange = { searchViewModel.updateTo(it) },
                    placeholder = "Select destination",
                    leadingIcon = R.drawable.baseline_explore_24
                )

                Spacer(modifier = Modifier.height(16.dp))

                SearchFieldLabel("Passengers")
                Spacer(modifier = Modifier.height(6.dp))
                PassengerSelector(
                    count = state.passengers,
                    onIncrement = { searchViewModel.incrementPassengers() },
                    onDecrement = { searchViewModel.decrementPassengers() }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        SearchFieldLabel("Departure date")
                        Spacer(modifier = Modifier.height(6.dp))
                        DateField(
                            value = state.departureDate,
                            onValueChange = { searchViewModel.updateDepartureDate(it) }
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        SearchFieldLabel("Return date")
                        Spacer(modifier = Modifier.height(6.dp))
                        DateField(
                            value = state.returnDate,
                            onValueChange = { searchViewModel.updateReturnDate(it) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onSearchClick(state.from, state.to, state.passengers) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D3142))
                ) {
                    Text(
                        "Search",
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    )
                }
            }
        }

        // Discount Banner
        DiscountBanner()
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun SearchFieldLabel(label: String) {
    Text(
        label,
        style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF2D3142))
    )
}

@Composable
fun SearchDropdownField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: Int
) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("New York", "Los Angeles", "Chicago", "Miami", "London")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(10.dp))
            .background(Color(0xFFFAFAFA))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(leadingIcon),
                contentDescription = null,
                tint = Color(0xFF9E9E9E),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                if (value.isEmpty()) placeholder else value,
                style = TextStyle(
                    fontSize = 14.sp,
                    color = if (value.isEmpty()) Color(0xFF9E9E9E) else Color(0xFF2D3142)
                ),
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { expanded = !expanded },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_flight_24),
                    contentDescription = "Expand",
                    tint = Color(0xFF9E9E9E),
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun PassengerSelector(
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(10.dp))
            .background(Color(0xFFFAFAFA))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.baseline_bookmark_24),
                    contentDescription = null,
                    tint = Color(0xFF9E9E9E),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = onDecrement, modifier = Modifier.size(28.dp)) {
                    Text("-", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9E9E9E)))
                }
            }
            Text(
                "$count Adult",
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF2D3142))
            )
            IconButton(onClick = onIncrement, modifier = Modifier.size(28.dp)) {
                Text("+", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6366F1)))
            }
        }
    }
}

@Composable
fun DateField(value: String, onValueChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(10.dp))
            .background(Color(0xFFFAFAFA))
            .padding(horizontal = 12.dp, vertical = 14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.baseline_bookmark_24),
                contentDescription = null,
                tint = Color(0xFF9E9E9E),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                if (value.isEmpty()) "-" else value,
                style = TextStyle(
                    fontSize = 13.sp,
                    color = if (value.isEmpty()) Color(0xFF9E9E9E) else Color(0xFF2D3142)
                )
            )
        }
    }
}

@Composable
fun DiscountBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF7FE7CC))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        "New Member",
                        style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Special Discount", style = TextStyle(fontSize = 13.sp, color = Color(0xFF9E9E9E)))
                Text(
                    "40% Off",
                    style = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D3142))
                )
            }

            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFEEF2FF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_explore_24),
                    contentDescription = null,
                    tint = Color(0xFF6366F1),
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF6366F1))
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("78%", style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White))
                    Text("Rem", style = TextStyle(fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f)))
                }
            }
        }
    }
}