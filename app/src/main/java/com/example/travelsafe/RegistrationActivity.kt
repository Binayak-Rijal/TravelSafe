package com.example.travelsafe

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelsafe.ui.theme.TravelSafeTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TravelSafeTheme {
                RegistrationBody()
            }
        }
    }
}

@Composable
fun RegistrationBody() {
    val auth = remember { FirebaseAuth.getInstance() }

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var confirmPasswordVisibility by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = context as Activity
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF10B981),
                        Color(0xFF059669),
                        Color(0xFF047857)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Header Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Logo Circle
                Surface(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape),
                    color = Color.White.copy(alpha = 0.25f)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "TS",
                            style = TextStyle(
                                fontSize = 36.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    "Create Account",
                    style = TextStyle(
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Sign up to start your safe journey",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Registration Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    // Social Login Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SocialMediaCard(
                            modifier = Modifier
                                .weight(1f)
                                .height(54.dp),
                            image = R.drawable.facebook,
                            label = "Facebook"
                        )

                        SocialMediaCard(
                            modifier = Modifier
                                .weight(1f)
                                .height(54.dp),
                            image = R.drawable.gmail,
                            label = "Gmail"
                        )
                    }

                    // Divider
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 28.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f))
                        Text(
                            "OR",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            style = TextStyle(
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray
                            )
                        )
                        HorizontalDivider(modifier = Modifier.weight(1f))
                    }

                    // Full Name Field
                    Text(
                        "Full Name",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1F2937)
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        shape = RoundedCornerShape(12.dp),
                        placeholder = { Text("Enter your full name") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF3F4F6),
                            unfocusedContainerColor = Color(0xFFF3F4F6),
                            focusedBorderColor = Color(0xFF10B981),
                            unfocusedBorderColor = Color.Transparent
                        ),
                        enabled = !isLoading
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Email Field
                    Text(
                        "Email",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1F2937)
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        shape = RoundedCornerShape(12.dp),
                        placeholder = { Text("abc@gmail.com") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF3F4F6),
                            unfocusedContainerColor = Color(0xFFF3F4F6),
                            focusedBorderColor = Color(0xFF10B981),
                            unfocusedBorderColor = Color.Transparent
                        ),
                        enabled = !isLoading
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Password Field
                    Text(
                        "Password",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1F2937)
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                                Icon(
                                    painter = if (passwordVisibility)
                                        painterResource(R.drawable.baseline_visibility_24)
                                    else
                                        painterResource(R.drawable.baseline_visibility_off_24),
                                    contentDescription = if (passwordVisibility) "Hide password" else "Show password"
                                )
                            }
                        },
                        visualTransformation = if (passwordVisibility) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        shape = RoundedCornerShape(12.dp),
                        placeholder = { Text("Create a password") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF3F4F6),
                            unfocusedContainerColor = Color(0xFFF3F4F6),
                            focusedBorderColor = Color(0xFF10B981),
                            unfocusedBorderColor = Color.Transparent
                        ),
                        enabled = !isLoading
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Confirm Password Field
                    Text(
                        "Confirm Password",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1F2937)
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        trailingIcon = {
                            IconButton(onClick = { confirmPasswordVisibility = !confirmPasswordVisibility }) {
                                Icon(
                                    painter = if (confirmPasswordVisibility)
                                        painterResource(R.drawable.baseline_visibility_24)
                                    else
                                        painterResource(R.drawable.baseline_visibility_off_24),
                                    contentDescription = if (confirmPasswordVisibility) "Hide password" else "Show password"
                                )
                            }
                        },
                        visualTransformation = if (confirmPasswordVisibility) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        shape = RoundedCornerShape(12.dp),
                        placeholder = { Text("Re-enter your password") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF3F4F6),
                            unfocusedContainerColor = Color(0xFFF3F4F6),
                            focusedBorderColor = Color(0xFF10B981),
                            unfocusedBorderColor = Color.Transparent
                        ),
                        enabled = !isLoading
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // Sign Up Button
                    Button(
                        onClick = {
                            when {
                                fullName.isEmpty() -> {
                                    Toast.makeText(
                                        context,
                                        "Please enter your full name",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                email.isEmpty() -> {
                                    Toast.makeText(
                                        context,
                                        "Please enter your email",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                password.isEmpty() -> {
                                    Toast.makeText(
                                        context,
                                        "Please enter a password",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                password.length < 6 -> {
                                    Toast.makeText(
                                        context,
                                        "Password must be at least 6 characters",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                password != confirmPassword -> {
                                    Toast.makeText(
                                        context,
                                        "Passwords do not match",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                else -> {
                                    isLoading = true

                                    auth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                val user = auth.currentUser
                                                val profileUpdates = UserProfileChangeRequest.Builder()
                                                    .setDisplayName(fullName)
                                                    .build()

                                                user?.updateProfile(profileUpdates)
                                                    ?.addOnCompleteListener { profileTask ->
                                                        isLoading = false
                                                        if (profileTask.isSuccessful) {
                                                            Toast.makeText(
                                                                context,
                                                                "Registration successful",
                                                                Toast.LENGTH_SHORT
                                                            ).show()

                                                            val sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE)
                                                            sharedPreferences.edit().apply {
                                                                putString("email", email)
                                                                putString("fullName", fullName)
                                                                apply()
                                                            }

                                                            val intent = Intent(context, MainActivity::class.java)
                                                            intent.putExtra("email", email)
                                                            intent.putExtra("fullName", fullName)
                                                            context.startActivity(intent)
                                                            activity.finish()
                                                        } else {
                                                            Toast.makeText(
                                                                context,
                                                                "Failed to update profile: ${profileTask.exception?.message}",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                    }
                                            } else {
                                                isLoading = false
                                                Toast.makeText(
                                                    context,
                                                    "Registration failed: ${task.exception?.message}",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF10B981)
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White
                            )
                        } else {
                            Text(
                                "Sign Up",
                                style = TextStyle(
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Sign In Link
            Text(
                buildAnnotatedString {
                    withStyle(SpanStyle(color = Color.White.copy(alpha = 0.9f))) {
                        append("Already have an account? ")
                    }
                    withStyle(SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) {
                        append("Sign in")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = !isLoading) {
                        val intent = Intent(context, LoginActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    .padding(vertical = 16.dp),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp
                )
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun SocialMediaCard(modifier: Modifier, image: Int, label: String) {
    OutlinedCard(
        modifier = modifier,
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.Transparent
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            width = 1.5.dp,
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFFE5E7EB), Color(0xFFD1D5DB))
            )
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                label,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF374151)
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegistration() {
    TravelSafeTheme {
        RegistrationBody()
    }
}