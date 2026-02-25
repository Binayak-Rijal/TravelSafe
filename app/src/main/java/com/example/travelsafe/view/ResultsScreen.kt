package com.example.travelsafe.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import coil.compose.AsyncImage
import com.example.travelsafe.R
import com.example.travelsafe.model.TripResult
import com.example.travelsafe.viewmodel.ResultsViewModel
//DEbugged
@Composable
fun ResultsScreen(
    from: String = "New York",
    to: String = "Los Angeles",
    passengers: Int = 1,
    onBackClick: () -> Unit = {},
    onResultClick: (TripResult) -> Unit = {},
    resultsViewModel: ResultsViewModel = viewModel()
) {
    val results = resultsViewModel.filteredResults

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Top Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2D3142))
                .padding(top = 40.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_flight_24),
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Search Results",
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("From", style = TextStyle(fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f)))
                            Text(from, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White))
                        }
                        Icon(
                            painter = painterResource(R.drawable.baseline_flight_24),
                            contentDescription = null,
                            tint = Color(0xFF7FE7CC),
                            modifier = Modifier.size(22.dp)
                        )
                        Column(horizontalAlignment = Alignment.End) {
                            Text("To", style = TextStyle(fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f)))
                            Text(to, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White))
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Passengers", style = TextStyle(fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f)))
                            Text("$passengers Adult", style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White))
                        }
                    }
                }
            }
        }

        // Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            resultsViewModel.filters.forEach { filter ->
                FilterChip(
                    selected = resultsViewModel.selectedFilter == filter,
                    onClick = { resultsViewModel.setFilter(filter) },
                    label = { Text(filter, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF6366F1),
                        selectedLabelColor = Color.White,
                        containerColor = Color.White,
                        labelColor = Color(0xFF2D3142)
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = resultsViewModel.selectedFilter == filter,
                        borderColor = Color(0xFFE0E0E0),
                        selectedBorderColor = Color(0xFF6366F1)
                    )
                )
            }
        }

        // Sort Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("${results.size} results found", style = TextStyle(fontSize = 13.sp, color = Color(0xFF9E9E9E)))
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Sort:", style = TextStyle(fontSize = 13.sp, color = Color(0xFF9E9E9E)))
                resultsViewModel.sortOptions.forEach { sort ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (resultsViewModel.sortBy == sort) Color(0xFF6366F1) else Color.White)
                            .clickable { resultsViewModel.setSort(sort) }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            sort,
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = if (resultsViewModel.sortBy == sort) Color.White else Color(0xFF2D3142)
                            )
                        )
                    }
                }
            }
        }

        // Results List
        if (results.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_explore_24),
                        contentDescription = null,
                        tint = Color(0xFFCCCCCC),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("No results found", style = TextStyle(fontSize = 16.sp, color = Color(0xFF9E9E9E)))
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(results) { result ->
                    TripResultCard(result = result, onClick = { onResultClick(result) })
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
fun TripResultCard(result: TripResult, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Airline + Type badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEEF2FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = result.airlineLogoUrl,
                            contentDescription = result.airline,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        result.airline,
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2D3142))
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            when (result.type) {
                                "Flight" -> Color(0xFFEEF2FF)
                                "Bus" -> Color(0xFFFFF3E0)
                                else -> Color(0xFFE8F5E9)
                            }
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        result.type,
                        style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = when (result.type) {
                                "Flight" -> Color(0xFF6366F1)
                                "Bus" -> Color(0xFFFF9800)
                                else -> Color(0xFF4CAF50)
                            }
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Route Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        result.departureTime,
                        style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D3142))
                    )
                    Text(result.originCode, style = TextStyle(fontSize = 12.sp, color = Color(0xFF9E9E9E)))
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(result.duration, style = TextStyle(fontSize = 11.sp, color = Color(0xFF9E9E9E)))
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    ) {
                        Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(Color(0xFF6366F1)))
                        Box(modifier = Modifier.weight(1f).height(1.dp).background(Color(0xFFCCCCCC)))
                        Icon(
                            painter = painterResource(R.drawable.baseline_flight_24),
                            contentDescription = null,
                            tint = Color(0xFF6366F1),
                            modifier = Modifier.size(16.dp)
                        )
                        Box(modifier = Modifier.weight(1f).height(1.dp).background(Color(0xFFCCCCCC)))
                        Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(Color(0xFFFFD700)))
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        result.arrivalTime,
                        style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D3142))
                    )
                    Text(result.destinationCode, style = TextStyle(fontSize = 12.sp, color = Color(0xFF9E9E9E)))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(14.dp))

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
                            tint = if (index < result.rating.toInt()) Color(0xFFFFD700) else Color(0xFFE0E0E0),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "${result.rating} (${result.reviews})",
                        style = TextStyle(fontSize = 12.sp, color = Color(0xFF9E9E9E))
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        result.price,
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6366F1))
                    )
                    Text("/per", style = TextStyle(fontSize = 11.sp, color = Color(0xFF9E9E9E)))
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Book", style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold))
                    }
                }
            }
        }
    }
}