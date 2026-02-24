package com.example.travelsafe.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.travelsafe.R
import com.example.travelsafe.model.Trip
import com.example.travelsafe.utils.TripManager

@Composable
fun TripPlannerScreen() {
    var showAddDialog by remember { mutableStateOf(false) }
    val trips = TripManager.trips

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
                .padding(top = 40.dp, bottom = 16.dp, start = 20.dp, end = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "My Trips",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                IconButton(onClick = { showAddDialog = true }) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_explore_24),
                        contentDescription = "Add Trip",
                        tint = Color.White
                    )
                }
            }
        }

        if (trips.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_flight_24),
                        contentDescription = null,
                        tint = Color(0xFFCCCCCC),
                        modifier = Modifier.size(72.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No trips planned yet",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF9E9E9E)
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Tap + to plan your first trip",
                        style = TextStyle(fontSize = 13.sp, color = Color(0xFFBBBBBB))
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { showAddDialog = true },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1))
                    ) {
                        Text("Plan a Trip")
                    }
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        "${trips.size} trip${if (trips.size > 1) "s" else ""} planned",
                        style = TextStyle(fontSize = 13.sp, color = Color(0xFF9E9E9E))
                    )
                }
                items(trips) { trip ->
                    TripCard(
                        trip = trip,
                        onDelete = { TripManager.removeTrip(trip) },
                        onStatusChange = { status -> TripManager.updateStatus(trip, status) }
                    )
                }
                item {
                    Button(
                        onClick = { showAddDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1))
                    ) {
                        Text("+ Add Another Trip")
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddTripDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { trip ->
                TripManager.addTrip(trip)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun TripCard(
    trip: Trip,
    onDelete: () -> Unit,
    onStatusChange: (String) -> Unit
) {
    val statusColor = when (trip.status) {
        "Ongoing" -> Color(0xFF4CAF50)
        "Completed" -> Color(0xFF9E9E9E)
        else -> Color(0xFF6366F1)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_flight_24),
                        contentDescription = null,
                        tint = Color(0xFF6366F1),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        trip.destination,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3142)
                        )
                    )
                }
                // Status badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(statusColor.copy(alpha = 0.1f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        trip.status,
                        style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = statusColor
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                TripInfoChip(
                    icon = R.drawable.baseline_bookmark_24,
                    label = "Depart",
                    value = trip.departureDate
                )
                Spacer(modifier = Modifier.width(12.dp))
                TripInfoChip(
                    icon = R.drawable.baseline_bookmark_24,
                    label = "Return",
                    value = trip.returnDate
                )
                Spacer(modifier = Modifier.width(12.dp))
                TripInfoChip(
                    icon = R.drawable.baseline_person_24,
                    label = "People",
                    value = "${trip.passengers}"
                )
            }

            if (trip.notes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    trip.notes,
                    style = TextStyle(
                        fontSize = 13.sp,
                        color = Color(0xFF9E9E9E),
                        lineHeight = 18.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF5F5F5))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Status change buttons
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (trip.status == "Planned") {
                        TextButton(
                            onClick = { onStatusChange("Ongoing") },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                "Start",
                                style = TextStyle(fontSize = 12.sp, color = Color(0xFF4CAF50))
                            )
                        }
                    }
                    if (trip.status == "Ongoing") {
                        TextButton(
                            onClick = { onStatusChange("Completed") },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                "Complete",
                                style = TextStyle(fontSize = 12.sp, color = Color(0xFF9E9E9E))
                            )
                        }
                    }
                }
                // Delete
                TextButton(
                    onClick = onDelete,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        "Remove",
                        style = TextStyle(fontSize = 12.sp, color = Color(0xFFFF5252))
                    )
                }
            }
        }
    }
}

@Composable
fun TripInfoChip(icon: Int, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = TextStyle(fontSize = 10.sp, color = Color(0xFF9E9E9E)))
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            value,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2D3142)
            )
        )
    }
}

@Composable
fun AddTripDialog(
    onDismiss: () -> Unit,
    onAdd: (Trip) -> Unit
) {
    var destination by remember { mutableStateOf("") }
    var departureDate by remember { mutableStateOf("") }
    var returnDate by remember { mutableStateOf("") }
    var passengers by remember { mutableStateOf("1") }
    var notes by remember { mutableStateOf("") }
    var destinationError by remember { mutableStateOf("") }
    var departureDateError by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                "Plan a New Trip",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3142)
                )
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                // Destination
                OutlinedTextField(
                    value = destination,
                    onValueChange = {
                        destination = it
                        destinationError = ""
                    },
                    label = { Text("Destination *") },
                    isError = destinationError.isNotEmpty(),
                    supportingText = {
                        if (destinationError.isNotEmpty())
                            Text(destinationError, color = Color.Red)
                    },
                    placeholder = { Text("e.g. Pokhara, Nepal") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6366F1),
                        unfocusedBorderColor = Color(0xFFE0E0E0)
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Departure Date
                OutlinedTextField(
                    value = departureDate,
                    onValueChange = {
                        departureDate = it
                        departureDateError = ""
                    },
                    label = { Text("Departure Date *") },
                    isError = departureDateError.isNotEmpty(),
                    supportingText = {
                        if (departureDateError.isNotEmpty())
                            Text(departureDateError, color = Color.Red)
                    },
                    placeholder = { Text("DD/MM/YYYY") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6366F1),
                        unfocusedBorderColor = Color(0xFFE0E0E0)
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Return Date
                OutlinedTextField(
                    value = returnDate,
                    onValueChange = { returnDate = it },
                    label = { Text("Return Date") },
                    placeholder = { Text("DD/MM/YYYY") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6366F1),
                        unfocusedBorderColor = Color(0xFFE0E0E0)
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Passengers
                OutlinedTextField(
                    value = passengers,
                    onValueChange = { if (it.all { c -> c.isDigit() }) passengers = it },
                    label = { Text("Passengers") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6366F1),
                        unfocusedBorderColor = Color(0xFFE0E0E0)
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (optional)") },
                    placeholder = { Text("Any special requirements...") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6366F1),
                        unfocusedBorderColor = Color(0xFFE0E0E0)
                    ),
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    var valid = true
                    if (destination.trim().isEmpty()) {
                        destinationError = "Destination is required"
                        valid = false
                    }
                    if (departureDate.trim().isEmpty()) {
                        departureDateError = "Departure date is required"
                        valid = false
                    }
                    if (valid) {
                        onAdd(
                            Trip(
                                destination = destination.trim(),
                                departureDate = departureDate.trim(),
                                returnDate = returnDate.trim().ifEmpty { "N/A" },
                                passengers = passengers.toIntOrNull() ?: 1,
                                notes = notes.trim()
                            )
                        )
                    }
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1))
            ) {
                Text("Add Trip", style = TextStyle(fontWeight = FontWeight.SemiBold))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color(0xFF9E9E9E))
            }
        }
    )
}