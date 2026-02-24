package com.example.travelsafe

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.travelsafe.model.Booking
import com.example.travelsafe.ui.theme.TravelSafeTheme
import com.example.travelsafe.viewmodel.BookingViewModel
import java.util.*

class BookingActivity : ComponentActivity() {

    private val bookingViewModel: BookingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val placeId = intent.getStringExtra("placeId") ?: ""
        val placeName = intent.getStringExtra("placeName") ?: ""
        val placeLocation = intent.getStringExtra("placeLocation") ?: ""
        val placePrice = intent.getStringExtra("placePrice") ?: ""
        val placeImageUrl = intent.getStringExtra("placeImageUrl") ?: ""
        val placeDescription = intent.getStringExtra("placeDescription") ?: ""
        val placeRating = intent.getFloatExtra("placeRating", 0f)

        setContent {
            TravelSafeTheme {
                BookingScreen(
                    placeId = placeId,
                    placeName = placeName,
                    placeLocation = placeLocation,
                    placePrice = placePrice,
                    placeImageUrl = placeImageUrl,
                    placeDescription = placeDescription,
                    placeRating = placeRating,
                    bookingViewModel = bookingViewModel,
                    onBack = { finish() },
                    onBookingSuccess = {
                        Toast.makeText(this, "Booking confirmed!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun BookingScreen(
    placeId: String,
    placeName: String,
    placeLocation: String,
    placePrice: String,
    placeImageUrl: String,
    placeDescription: String,
    placeRating: Float,
    bookingViewModel: BookingViewModel,
    onBack: () -> Unit,
    onBookingSuccess: () -> Unit
) {
    var checkInDate by remember { mutableStateOf("") }
    var checkOutDate by remember { mutableStateOf("") }
    var guests by remember { mutableStateOf(1) }
    var specialRequests by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showSuccessScreen by remember { mutableStateOf(false) }
    var bookingId by remember { mutableStateOf("") }

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Date picker helper
    fun showDatePicker(onDateSelected: (String) -> Unit) {
        val cal = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, day ->
                onDateSelected(String.format("%02d/%02d/%04d", day, month + 1, year))
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).also {
            it.datePicker.minDate = System.currentTimeMillis()
            it.show()
        }
    }

    if (showSuccessScreen) {
        BookingSuccessScreen(
            bookingId = bookingId,
            placeName = placeName,
            checkIn = checkInDate,
            checkOut = checkOutDate,
            guests = guests,
            onDone = onBookingSuccess
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Place Image Header
        Box(modifier = Modifier.fillMaxWidth().height(280.dp)) {
            if (placeImageUrl.isNotEmpty()) {
                AsyncImage(
                    model = placeImageUrl,
                    contentDescription = placeName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color(0xFF6366F1)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_explore_24),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(64.dp)
                    )
                }
            }

            // Gradient overlay
            Box(
                modifier = Modifier.fillMaxSize().background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.4f), Color.Transparent, Color.Black.copy(alpha = 0.6f))
                    )
                )
            )

            // Back button
            IconButton(
                onClick = onBack,
                modifier = Modifier.padding(top = 40.dp, start = 8.dp).align(Alignment.TopStart)
            ) {
                Box(
                    modifier = Modifier.size(38.dp).background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_flight_24),
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Place info on image
            Column(
                modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
            ) {
                Text(placeName, style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painter = painterResource(R.drawable.baseline_explore_24), contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(placeLocation, style = TextStyle(fontSize = 13.sp, color = Color.White))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("⭐ $placeRating", style = TextStyle(fontSize = 13.sp, color = Color.White))
                }
            }

            // Price
            Box(
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
                    .background(Color(0xFF6366F1), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(placePrice, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White))
            }
        }

        // Booking Form
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Complete Your Booking",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D3142))
            )

            if (placeDescription.isNotEmpty()) {
                Text(
                    placeDescription,
                    style = TextStyle(fontSize = 13.sp, color = Color(0xFF666666), lineHeight = 18.sp)
                )
            }

            // Personal Info Card
            BookingCard(title = "Personal Information") {
                BookingTextField(
                    label = "Full Name *",
                    value = fullName,
                    onValueChange = { fullName = it },
                    placeholder = "Enter your full name"
                )
                Spacer(modifier = Modifier.height(10.dp))
                BookingTextField(
                    label = "Phone Number *",
                    value = phone,
                    onValueChange = { phone = it },
                    placeholder = "Enter your phone number"
                )
            }

            // Trip Details Card
            BookingCard(title = "Trip Details") {
                // Check-in
                Text("Check-in Date *", style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF374151)))
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedButton(
                    onClick = { showDatePicker { checkInDate = it } },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (checkInDate.isEmpty()) Color(0xFF9E9E9E) else Color(0xFF2D3142)
                    )
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(if (checkInDate.isEmpty()) "Select check-in date" else checkInDate, style = TextStyle(fontSize = 14.sp))
                        Icon(painter = painterResource(R.drawable.baseline_flight_24), contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Check-out
                Text("Check-out Date *", style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF374151)))
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedButton(
                    onClick = { showDatePicker { checkOutDate = it } },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (checkOutDate.isEmpty()) Color(0xFF9E9E9E) else Color(0xFF2D3142)
                    )
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(if (checkOutDate.isEmpty()) "Select check-out date" else checkOutDate, style = TextStyle(fontSize = 14.sp))
                        Icon(painter = painterResource(R.drawable.baseline_flight_24), contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Guests
                Text("Number of Guests", style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF374151)))
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = { if (guests > 1) guests-- },
                        modifier = Modifier.size(44.dp).background(Color(0xFFEEEEFF), RoundedCornerShape(12.dp))
                    ) {
                        Text("-", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6366F1)))
                    }
                    Text(
                        "$guests Guest${if (guests > 1) "s" else ""}",
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2D3142))
                    )
                    IconButton(
                        onClick = { if (guests < 20) guests++ },
                        modifier = Modifier.size(44.dp).background(Color(0xFF6366F1), RoundedCornerShape(12.dp))
                    ) {
                        Text("+", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White))
                    }
                }
            }

            // Special Requests
            BookingCard(title = "Special Requests (Optional)") {
                OutlinedTextField(
                    value = specialRequests,
                    onValueChange = { specialRequests = it },
                    placeholder = { Text("Any special requirements...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 4,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6366F1),
                        unfocusedBorderColor = Color(0xFFE5E7EB)
                    )
                )
            }

            // Summary Card
            BookingCard(title = "Booking Summary") {
                SummaryRow("Place", placeName)
                SummaryRow("Location", placeLocation)
                SummaryRow("Price", placePrice)
                SummaryRow("Check-in", if (checkInDate.isEmpty()) "Not selected" else checkInDate)
                SummaryRow("Check-out", if (checkOutDate.isEmpty()) "Not selected" else checkOutDate)
                SummaryRow("Guests", "$guests")
            }

            // Book Button
            Button(
                onClick = {
                    when {
                        fullName.trim().isEmpty() -> Toast.makeText(context, "Please enter your name", Toast.LENGTH_SHORT).show()
                        phone.trim().isEmpty() -> Toast.makeText(context, "Please enter your phone number", Toast.LENGTH_SHORT).show()
                        checkInDate.isEmpty() -> Toast.makeText(context, "Please select check-in date", Toast.LENGTH_SHORT).show()
                        checkOutDate.isEmpty() -> Toast.makeText(context, "Please select check-out date", Toast.LENGTH_SHORT).show()
                        else -> showConfirmDialog = true
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(22.dp), color = Color.White)
                } else {
                    Text("Confirm Booking", style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // Confirm Dialog
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            title = {
                Text("Confirm Booking", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D3142)))
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Please review your booking:", style = TextStyle(fontSize = 14.sp, color = Color(0xFF666666)))
                    Spacer(modifier = Modifier.height(4.dp))
                    SummaryRow("Place", placeName)
                    SummaryRow("Check-in", checkInDate)
                    SummaryRow("Check-out", checkOutDate)
                    SummaryRow("Guests", "$guests")
                    SummaryRow("Price", placePrice)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDialog = false
                        isLoading = true
                        bookingViewModel.createBooking(
                            Booking(
                                placeId = placeId,
                                placeName = placeName,
                                placeLocation = placeLocation,
                                placeImageUrl = placeImageUrl,
                                checkInDate = checkInDate,
                                checkOutDate = checkOutDate,
                                guests = guests,
                                totalPrice = placePrice,
                                specialRequests = specialRequests
                            )
                        ) { success, message ->
                            isLoading = false
                            if (success) {
                                // Extract booking ID from message
                                bookingId = message.substringAfter("ID: ").trim()
                                showSuccessScreen = true
                            } else {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Confirm", style = TextStyle(fontWeight = FontWeight.Bold))
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancel", color = Color(0xFF9E9E9E))
                }
            }
        )
    }
}

@Composable
fun BookingSuccessScreen(
    bookingId: String,
    placeName: String,
    checkIn: String,
    checkOut: String,
    guests: Int,
    onDone: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Success Icon
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color(0xFFEEFFF5), RoundedCornerShape(50.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("✓", style = TextStyle(fontSize = 48.sp, color = Color(0xFF22C55E), fontWeight = FontWeight.Bold))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Booking Confirmed!",
            style = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D3142))
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Your booking has been submitted successfully.",
            style = TextStyle(fontSize = 14.sp, color = Color(0xFF9E9E9E)),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Booking Details Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8FF)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    "Booking Details",
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D3142))
                )
                HorizontalDivider(color = Color(0xFFEEEEEE))
                SummaryRow("Booking ID", bookingId.take(12) + "...")
                SummaryRow("Place", placeName)
                SummaryRow("Check-in", checkIn)
                SummaryRow("Check-out", checkOut)
                SummaryRow("Guests", "$guests")
                SummaryRow("Status", "Pending Confirmation")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "The admin will confirm your booking shortly.",
            style = TextStyle(fontSize = 13.sp, color = Color(0xFF9E9E9E)),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onDone,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1))
        ) {
            Text("Done", style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White))
        }
    }
}

@Composable
fun BookingCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D3142)))
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun BookingTextField(label: String, value: String, onValueChange: (String) -> Unit, placeholder: String) {
    Text(label, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF374151)))
    Spacer(modifier = Modifier.height(6.dp))
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, style = TextStyle(fontSize = 13.sp)) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF6366F1),
            unfocusedBorderColor = Color(0xFFE5E7EB),
            focusedContainerColor = Color(0xFFF5F5FF),
            unfocusedContainerColor = Color(0xFFF9FAFB)
        )
    )
}

@Composable
fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = TextStyle(fontSize = 13.sp, color = Color(0xFF9E9E9E)))
        Text(value, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2D3142)))
    }
}