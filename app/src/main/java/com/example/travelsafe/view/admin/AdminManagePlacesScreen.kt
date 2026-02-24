@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.travelsafe.view.admin

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelsafe.R
import com.example.travelsafe.model.Place
import com.example.travelsafe.viewmodel.AdminViewModel

@Composable
fun AdminManagePlacesScreen(
    adminViewModel: AdminViewModel,
    padding: PaddingValues
) {
    val places = adminViewModel.places
    val context = LocalContext.current
    var showAddDialog by remember { mutableStateOf(false) }
    var editingPlace by remember { mutableStateOf<Place?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var showDeleteConfirm by remember { mutableStateOf<Place?>(null) }

    LaunchedEffect(Unit) {
        adminViewModel.loadAllPlaces()
    }

    val filtered = if (searchQuery.isEmpty()) places.toList()
    else places.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
                it.location.contains(searchQuery, ignoreCase = true) ||
                it.category.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0FF))
            .padding(padding)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E1B4B))
                .padding(top = 40.dp, bottom = 16.dp, start = 20.dp, end = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Manage Places", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White))
                    Text("${places.size} places total", style = TextStyle(fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f)))
                }
                // Add Button
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = Color(0xFF818CF8),
                    modifier = Modifier.size(42.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_explore_24),
                        contentDescription = "Add",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search places...") },
            leadingIcon = {
                Icon(painter = painterResource(R.drawable.baseline_search_24), contentDescription = null, tint = Color(0xFF4338CA))
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(painter = painterResource(R.drawable.baseline_explore_24), contentDescription = "Clear", tint = Color(0xFF9E9E9E))
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4338CA),
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            singleLine = true
        )

        if (filtered.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_explore_24),
                        contentDescription = null,
                        tint = Color(0xFFCCCCCC),
                        modifier = Modifier.size(60.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        if (searchQuery.isEmpty()) "No places yet. Tap + to add one."
                        else "No results for \"$searchQuery\"",
                        style = TextStyle(fontSize = 14.sp, color = Color(0xFF9E9E9E))
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filtered) { place ->
                    AdminPlaceCard(
                        place = place,
                        onEdit = { editingPlace = place },
                        onDelete = { showDeleteConfirm = place }
                    )
                }
            }
        }
    }

    // Add Dialog
    if (showAddDialog) {
        PlaceFormDialog(
            title = "Add New Place",
            place = null,
            onDismiss = { showAddDialog = false },
            onSave = { newPlace ->
                adminViewModel.addPlace(newPlace) { success, message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    if (success) {
                        showAddDialog = false
                        adminViewModel.loadAllPlaces()
                    }
                }
            }
        )
    }

    // Edit Dialog
    if (editingPlace != null) {
        PlaceFormDialog(
            title = "Edit Place",
            place = editingPlace,
            onDismiss = { editingPlace = null },
            onSave = { updatedPlace ->
                adminViewModel.updatePlace(updatedPlace) { success, message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    if (success) {
                        editingPlace = null
                        adminViewModel.loadAllPlaces()
                    }
                }
            }
        )
    }

    // Delete Confirmation Dialog
    if (showDeleteConfirm != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = null },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            title = {
                Text("Delete Place", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E1B4B)))
            },
            text = {
                Text(
                    "Are you sure you want to delete \"${showDeleteConfirm?.name}\"? This cannot be undone.",
                    style = TextStyle(fontSize = 14.sp, color = Color(0xFF666666))
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val placeId = showDeleteConfirm!!.placeId
                        adminViewModel.deletePlace(placeId) { success, message ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                        showDeleteConfirm = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Delete", style = TextStyle(fontWeight = FontWeight.Bold))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = null }) {
                    Text("Cancel", color = Color(0xFF9E9E9E))
                }
            }
        )
    }
}

@Composable
fun AdminPlaceCard(
    place: Place,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val categoryColor = when (place.category) {
        "Beach" -> Color(0xFF0EA5E9)
        "Mountain" -> Color(0xFF16A34A)
        "Culture" -> Color(0xFFD97706)
        "Food" -> Color(0xFFDC2626)
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
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            place.name,
                            style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E1B4B))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        // Category badge
                        Box(
                            modifier = Modifier
                                .background(categoryColor.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                place.category,
                                style = TextStyle(fontSize = 10.sp, color = categoryColor, fontWeight = FontWeight.Medium)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_explore_24),
                            contentDescription = null,
                            tint = Color(0xFF9E9E9E),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(place.location, style = TextStyle(fontSize = 12.sp, color = Color(0xFF9E9E9E)))
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(place.price, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4338CA)))
                    Text("⭐ ${place.rating} (${place.reviews})", style = TextStyle(fontSize = 11.sp, color = Color(0xFF9E9E9E)))
                }
            }

            if (place.description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    place.description.take(80) + if (place.description.length > 80) "..." else "",
                    style = TextStyle(fontSize = 12.sp, color = Color(0xFF666666), lineHeight = 16.sp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF5F5F5))
            Spacer(modifier = Modifier.height(8.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                // Edit
                OutlinedButton(
                    onClick = onEdit,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF4338CA)),
                    border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Icon(painter = painterResource(R.drawable.baseline_person_24), contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Edit", style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold))
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Delete
                Button(
                    onClick = onDelete,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252)),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Icon(painter = painterResource(R.drawable.baseline_bookmark_24), contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Delete", style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold))
                }
            }
        }
    }
}

@Composable
fun PlaceFormDialog(
    title: String,
    place: Place?,
    onDismiss: () -> Unit,
    onSave: (Place) -> Unit
) {
    var name by remember { mutableStateOf(place?.name ?: "") }
    var location by remember { mutableStateOf(place?.location ?: "") }
    var description by remember { mutableStateOf(place?.description ?: "") }
    var price by remember { mutableStateOf(place?.price ?: "") }
    var rating by remember { mutableStateOf(place?.rating?.toString() ?: "") }
    var reviews by remember { mutableStateOf(place?.reviews?.toString() ?: "") }
    var imageUrl by remember { mutableStateOf(place?.imageUrl ?: "") }
    var category by remember { mutableStateOf(place?.category ?: "General") }
    var highlights by remember { mutableStateOf(place?.highlights?.joinToString(", ") ?: "") }

    val categories = listOf("General", "Beach", "Mountain", "Culture", "Food")
    var expandedDropdown by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(title, style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E1B4B)))
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Name
                AdminFormField(
                    label = "Place Name *",
                    value = name,
                    onValueChange = { name = it; nameError = "" },
                    placeholder = "e.g. Royal Beach",
                    error = nameError
                )

                // Location
                AdminFormField(
                    label = "Location *",
                    value = location,
                    onValueChange = { location = it },
                    placeholder = "e.g. Pokhara, Nepal"
                )

                // Category Dropdown
                Text("Category", style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF374151)))
                ExposedDropdownMenuBox(
                    expanded = expandedDropdown,
                    onExpandedChange = { expandedDropdown = !expandedDropdown }
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4338CA),
                            unfocusedBorderColor = Color(0xFFE5E7EB)
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expandedDropdown,
                        onDismissRequest = { expandedDropdown = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    category = cat
                                    expandedDropdown = false
                                }
                            )
                        }
                    }
                }

                // Price
                AdminFormField(
                    label = "Price",
                    value = price,
                    onValueChange = { price = it },
                    placeholder = "e.g. \$10/person"
                )

                // Rating
                AdminFormField(
                    label = "Rating (0-5)",
                    value = rating,
                    onValueChange = { if (it.isEmpty() || it.toFloatOrNull() != null) rating = it },
                    placeholder = "e.g. 4.5"
                )

                // Reviews
                AdminFormField(
                    label = "Number of Reviews",
                    value = reviews,
                    onValueChange = { if (it.isEmpty() || it.toIntOrNull() != null) reviews = it },
                    placeholder = "e.g. 250"
                )

                // Image URL
                AdminFormField(
                    label = "Cloudinary Image URL",
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    placeholder = "https://res.cloudinary.com/..."
                )

                // Highlights
                AdminFormField(
                    label = "Highlights (comma separated)",
                    value = highlights,
                    onValueChange = { highlights = it },
                    placeholder = "e.g. Beach view, Water sports, Sunset",
                    maxLines = 3
                )

                // Description
                AdminFormField(
                    label = "Description",
                    value = description,
                    onValueChange = { description = it },
                    placeholder = "Write a short description...",
                    maxLines = 4
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.trim().isEmpty()) {
                        nameError = "Name is required"
                        return@Button
                    }
                    if (location.trim().isEmpty()) {
                        return@Button
                    }
                    val highlightList = if (highlights.isEmpty()) emptyList()
                    else highlights.split(",").map { it.trim() }.filter { it.isNotEmpty() }

                    onSave(
                        Place(
                            placeId = place?.placeId ?: "",
                            name = name.trim(),
                            location = location.trim(),
                            description = description.trim(),
                            price = price.trim(),
                            rating = rating.toFloatOrNull() ?: 0f,
                            reviews = reviews.toIntOrNull() ?: 0,
                            imageUrl = imageUrl.trim(),
                            category = category,
                            highlights = highlightList
                        )
                    )
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4338CA))
            ) {
                Text("Save", style = TextStyle(fontWeight = FontWeight.Bold))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color(0xFF9E9E9E))
            }
        }
    )
}

@Composable
fun AdminFormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    error: String = "",
    maxLines: Int = 1
) {
    Column {
        Text(label, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF374151)))
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, style = TextStyle(fontSize = 13.sp)) },
            isError = error.isNotEmpty(),
            supportingText = { if (error.isNotEmpty()) Text(error, color = Color.Red, style = TextStyle(fontSize = 11.sp)) },
            shape = RoundedCornerShape(10.dp),
            maxLines = maxLines,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4338CA),
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedContainerColor = Color(0xFFF5F5FF),
                unfocusedContainerColor = Color(0xFFF9FAFB)
            )
        )
    }
}