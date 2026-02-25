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
import androidx.compose.ui.text.style.TextAlign
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
        val placeName = intent.getStringExtra("placeName") ?: "Unknown Place"
        val placeLocation = intent.getStringExtra("placeLocation") ?: ""
        val placePrice = intent.getStringExtra("placePrice") ?: ""
        val placeImageUrl = intent.getStringExtra("placeImageUrl") ?: ""
        val placeDescription = intent.getStringExtra("placeDescription") ?: ""
        val placeRating = intent.getFloatExtra("placeRating", 0f)

        setContent {
            TravelSafeTheme {
                BookingScreen(
                    activity = this,
                    placeId = placeId,
                    placeName = placeName,
                    placeLocation = placeLocation,
                    placePrice = placePrice,
                    placeImageUrl = placeImageUrl,
                    placeDescription = placeDescription,
                    placeRating = placeRating,
                    bookingViewModel = bookingViewModel,
                    onBack = { finish() },
                    onBookingSuccess = { finish() }
                )
            }
        }
    }
}

@Composable
fun BookingScreen(
    activity: ComponentActivity,
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
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var checkInDate by remember { mutableStateOf("") }
    var checkOutDate by remember { mutableStateOf("") }
    var guests by remember { mutableStateOf(1) }
    var specialRequests by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showSuccessScreen by remember { mutableStateOf(false) }
    var confirmedBookingId by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    // ✅ Date picker using the actual Activity — this is the fix
    fun pickDate(onPicked: (String) -> Unit) {
        val cal = Calendar.getInstance()
        DatePickerDialog(
            activity,
            { _, year, month, day ->
                onPicked(String.format("%02d/%02d/%04d", day, month + 1, year))
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).also { dialog ->
            dialog.datePicker.minDate = System.currentTimeMillis() - 1000
            dialog.show()
        }
    }

    if (showSuccessScreen) {
        BookingSuccessScreen(
            bookingId = confirmedBookingId,
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
            .background(Color(0xFFF8F8FF))
    ) {
        // Place Image Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        ) {
            if (placeImageUrl.isNotEmpty()) {
                AsyncImage(
                    model = placeImageUrl,
                    contentDescription = placeName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFF6366F1), Color(0xFF8B5CF6))
                            )
                        ),
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

            // Dark overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.35f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.55f)
                            )
                        )
                    )
            )

            // Back Button
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 44.dp, start = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(Color.Black.copy(alpha = 0.45f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("←", style = TextStyle(fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.Bold))
                }
            }

            // Place details overlay
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    placeName,
                    style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_explore_24),
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.85f),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(placeLocation, style = TextStyle(fontSize = 13.sp, color = Color.White.copy(alpha = 0.85f)))
                    if (placeRating > 0f) {
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("⭐ $placeRating", style = TextStyle(fontSize = 13.sp, color = Color.White))
                    }
                }
            }

            // Price tag
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .background(Color(0xFF6366F1), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(placePrice, style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White))
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
                    style = TextStyle(fontSize = 13.sp, color = Color(0xFF777777), lineHeight = 18.sp)
                )
            }

            // Personal Info
            BookingCard(title = "Your Information") {
                BookingField(label = "Full Name *", value = fullName, onValueChange = { fullName = it }, placeholder = "Enter your full name")
                Spacer(modifier = Modifier.height(10.dp))
                BookingField(label = "Phone Number *", value = phone, onValueChange = { phone = it }, placeholder = "Enter your phone number")
            }

            // Trip Details
            BookingCard(title = "Trip Details") {

                // Check-in date
                Text("Check-in Date *", style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF374151)))
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedButton(
                    onClick = { pickDate { checkInDate = it } },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (checkInDate.isEmpty()) Color(0xFF9E9E9E) else Color(0xFF2D3142)
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(if (checkInDate.isEmpty()) "Tap to select check-in date" else checkInDate, style = TextStyle(fontSize = 14.sp))
                        Text("📅", style = TextStyle(fontSize = 16.sp))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Check-out date
                Text("Check-out Date *", style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF374151)))
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedButton(
                    onClick = { pickDate { checkOutDate = it } },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (checkOutDate.isEmpty()) Color(0xFF9E9E9E) else Color(0xFF2D3142)
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(if (checkOutDate.isEmpty()) "Tap to select check-out date" else checkOutDate, style = TextStyle(fontSize = 14.sp))
                        Text("📅", style = TextStyle(fontSize = 16.sp))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Guests
                Text("Number of Guests", style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF374151)))
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { if (guests > 1) guests-- },
                        modifier = Modifier.size(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEEEEFF)),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("−", style = TextStyle(fontSize = 22.sp, color = Color(0xFF6366F1), fontWeight = FontWeight.Bold))
                    }
                    Text(
                        "$guests Guest${if (guests != 1) "s" else ""}",
                        style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2D3142))
                    )
                    Button(
                        onClick = { if (guests < 20) guests++ },
                        modifier = Modifier.size(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("+", style = TextStyle(fontSize = 22.sp, color = Color.White, fontWeight = FontWeight.Bold))
                    }
                }
            }

            // Special Requests
            BookingCard(title = "Special Requests (Optional)") {
                OutlinedTextField(
                    value = specialRequests,
                    onValueChange = { specialRequests = it },
                    placeholder = { Text("Any special requirements or requests...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 4,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6366F1),
                        unfocusedBorderColor = Color(0xFFE5E7EB)
                    )
                )
            }

            // Booking Summary
            BookingCard(title = "Booking Summary") {
                SummaryRow(label = "Place", value = placeName)
                Spacer(modifier = Modifier.height(6.dp))
                SummaryRow(label = "Location", value = placeLocation)
                Spacer(modifier = Modifier.height(6.dp))
                SummaryRow(label = "Price", value = placePrice)
                Spacer(modifier = Modifier.height(6.dp))
                SummaryRow(label = "Check-in", value = checkInDate.ifEmpty { "Not selected" })
                Spacer(modifier = Modifier.height(6.dp))
                SummaryRow(label = "Check-out", value = checkOutDate.ifEmpty { "Not selected" })
                Spacer(modifier = Modifier.height(6.dp))
                SummaryRow(label = "Guests", value = "$guests")
            }

            // Confirm Booking Button
            Button(
                onClick = {
                    when {
                        fullName.trim().isEmpty() -> Toast.makeText(activity, "Please enter your name", Toast.LENGTH_SHORT).show()
                        phone.trim().isEmpty() -> Toast.makeText(activity, "Please enter your phone number", Toast.LENGTH_SHORT).show()
                        checkInDate.isEmpty() -> Toast.makeText(activity, "Please select check-in date", Toast.LENGTH_SHORT).show()
                        checkOutDate.isEmpty() -> Toast.makeText(activity, "Please select check-out date", Toast.LENGTH_SHORT).show()
                        else -> showConfirmDialog = true
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(22.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Confirm Booking", style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Confirmation Dialog
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            title = {
                Text("Confirm Booking", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D3142)))
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Please confirm your booking details:", style = TextStyle(fontSize = 14.sp, color = Color(0xFF666666)))
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
                                // message is "Booking confirmed! ID: xxxx"
                                confirmedBookingId = message.substringAfter("ID: ").trim()
                                showSuccessScreen = true
                            } else {
                                Toast.makeText(activity, "Error: $message", Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Yes, Book Now", style = TextStyle(fontWeight = FontWeight.Bold))
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
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Green checkmark circle
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color(0xFFDCFCE7), RoundedCornerShape(50.dp)),
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
            "Your booking has been submitted.\nThe admin will confirm it shortly.",
            style = TextStyle(fontSize = 14.sp, color = Color(0xFF9E9E9E), textAlign = TextAlign.Center),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8FF)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    "Booking Details",
                    style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D3142))
                )
                HorizontalDivider(color = Color(0xFFEEEEEE))
                if (bookingId.isNotEmpty()) {
                    SummaryRow("Booking ID", bookingId.take(16) + if (bookingId.length > 16) "..." else "")
                }
                SummaryRow("Place", placeName)
                SummaryRow("Check-in", checkIn)
                SummaryRow("Check-out", checkOut)
                SummaryRow("Guests", "$guests")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Status", style = TextStyle(fontSize = 13.sp, color = Color(0xFF9E9E9E)))
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFF3E0), RoundedCornerShape(20.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text("Pending Confirmation", style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD97706)))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onDone,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1))
        ) {
            Text("View My Bookings", style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White))
        }
    }
}

// ── Shared helper composables ──────────────────────────────────────────────────

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
fun BookingField(label: String, value: String, onValueChange: (String) -> Unit, placeholder: String) {
    Text(label, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF374151)))
    Spacer(modifier = Modifier.height(6.dp))
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, style = TextStyle(fontSize = 13.sp, color = Color(0xFF9E9E9E))) },
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
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = TextStyle(fontSize = 13.sp, color = Color(0xFF9E9E9E)))
        Text(
            value,
            style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2D3142)),
            textAlign = TextAlign.End
        )
    }
}