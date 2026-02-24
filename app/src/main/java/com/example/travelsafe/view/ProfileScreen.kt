package com.example.travelsafe.view

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelsafe.LoginActivity
import com.example.travelsafe.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

@Composable
fun ProfileScreen() {
    var showEditDialog by remember { mutableStateOf(false) }
    var showAbout by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val auth = remember { FirebaseAuth.getInstance() }
    val currentUser = auth.currentUser
    var displayName by remember { mutableStateOf(currentUser?.displayName ?: "User") }
    val email = currentUser?.email ?: ""
    val scrollState = rememberScrollState()

    if (showAbout) {
        AboutScreen(onBackClick = { showAbout = false })
        return
    }

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
                .padding(top = 40.dp, bottom = 16.dp, start = 20.dp, end = 16.dp)
        ) {
            Text("Profile", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White))
        }

        Spacer(modifier = Modifier.height(30.dp))

        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier.size(90.dp).clip(CircleShape).background(Color(0xFFE8E8E8)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_person_24),
                    contentDescription = null,
                    tint = Color(0xFF9E9E9E),
                    modifier = Modifier.size(55.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(displayName, style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D3142)))
            Spacer(modifier = Modifier.height(4.dp))
            Text(email, style = TextStyle(fontSize = 14.sp, color = Color(0xFF9E9E9E)))
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = { showEditDialog = true },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF6366F1)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF6366F1))
            ) {
                Icon(painter = painterResource(R.drawable.baseline_person_24), contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Edit Profile", style = TextStyle(fontSize = 13.sp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Account Info
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Account Info", style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF9E9E9E)))
                Spacer(modifier = Modifier.height(12.dp))
                ProfileInfoRow("Full Name", displayName)
                HorizontalDivider(color = Color(0xFFF5F5F5), modifier = Modifier.padding(vertical = 8.dp))
                ProfileInfoRow("Email", email)
                HorizontalDivider(color = Color(0xFFF5F5F5), modifier = Modifier.padding(vertical = 8.dp))
                ProfileInfoRow("Status", if (currentUser?.isEmailVerified == true) "Verified ✓" else "Not Verified")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Menu Items
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                ProfileMenuItem(R.drawable.baseline_bookmark_24, "My Bookmarks") { }
                HorizontalDivider(color = Color(0xFFF5F5F5))
                ProfileMenuItem(R.drawable.baseline_flight_24, "My Trips") { }
                HorizontalDivider(color = Color(0xFFF5F5F5))
                ProfileMenuItem(R.drawable.baseline_explore_24, "Help & About") { showAbout = true }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                auth.signOut()
                val intent = Intent(context, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252))
        ) {
            Text("Log Out", style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White))
        }

        Spacer(modifier = Modifier.height(30.dp))
    }

    if (showEditDialog) {
        EditProfileDialog(
            currentName = displayName,
            currentEmail = email,
            onDismiss = { showEditDialog = false },
            onSave = { newName ->
                val updates = UserProfileChangeRequest.Builder().setDisplayName(newName).build()
                currentUser?.updateProfile(updates)?.addOnSuccessListener {
                    displayName = newName
                }
                showEditDialog = false
            }
        )
    }
}

@Composable
fun EditProfileDialog(currentName: String, currentEmail: String, onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var nameInput by remember { mutableStateOf(currentName) }
    var nameError by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp),
        title = { Text("Edit Profile", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D3142))) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it; nameError = "" },
                    label = { Text("Full Name") },
                    isError = nameError.isNotEmpty(),
                    supportingText = { if (nameError.isNotEmpty()) Text(nameError, color = Color.Red) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF6366F1), unfocusedBorderColor = Color(0xFFE0E0E0)),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = currentEmail,
                    onValueChange = {},
                    enabled = false,
                    label = { Text("Email") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(disabledBorderColor = Color(0xFFE0E0E0), disabledTextColor = Color(0xFF9E9E9E)),
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Email cannot be changed here", style = TextStyle(fontSize = 11.sp, color = Color(0xFF9E9E9E)))
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nameInput.trim().isEmpty()) nameError = "Name cannot be empty"
                    else onSave(nameInput.trim())
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1))
            ) {
                Text("Save", style = TextStyle(fontWeight = FontWeight.SemiBold))
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel", color = Color(0xFF9E9E9E)) } }
    )
}

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = TextStyle(fontSize = 14.sp, color = Color(0xFF9E9E9E)))
        Text(value, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF2D3142)))
    }
}

@Composable
fun ProfileMenuItem(icon: Int, title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painter = painterResource(icon), contentDescription = title, tint = Color(0xFF6366F1), modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Color(0xFF2D3142)), modifier = Modifier.weight(1f))
            Icon(painter = painterResource(R.drawable.baseline_explore_24), contentDescription = null, tint = Color(0xFFCCCCCC), modifier = Modifier.size(16.dp))
        }
    }
}