package com.example.travelsafe

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import com.example.travelsafe.viewmodel.AuthViewModel

class LoginActivity : ComponentActivity() {

    // ✅ ViewModel instantiated here
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TravelSafeTheme {
                LoginBody(authViewModel = authViewModel)
            }
        }
    }
}

@Composable
fun LoginBody(authViewModel: AuthViewModel = AuthViewModel()) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var visibility by remember { mutableStateOf(false) }
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
                        Color(0xFF6366F1),
                        Color(0xFF8B5CF6),
                        Color(0xFFA855F7)
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
            Spacer(modifier = Modifier.height(80.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    modifier = Modifier.size(90.dp).clip(CircleShape),
                    color = Color.White.copy(alpha = 0.25f)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text("TS", style = TextStyle(fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = Color.White))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text("Welcome Back!", style = TextStyle(fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.White))

                Spacer(modifier = Modifier.height(8.dp))

                Text("Login to access your travel plans", style = TextStyle(fontSize = 16.sp, color = Color.White.copy(alpha = 0.85f)))
            }

            Spacer(modifier = Modifier.height(48.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {

                    // Social Login Buttons
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        SocialMediaCard(
                            modifier = Modifier.weight(1f).height(54.dp),
                            image = R.drawable.facebook,
                            label = "Facebook",
                            enabled = !isLoading,
                            onClick = { Toast.makeText(context, "Facebook login coming soon", Toast.LENGTH_SHORT).show() }
                        )
                        SocialMediaCard(
                            modifier = Modifier.weight(1f).height(54.dp),
                            image = R.drawable.gmail,
                            label = "Gmail",
                            enabled = !isLoading,
                            onClick = { Toast.makeText(context, "Google login coming soon", Toast.LENGTH_SHORT).show() }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 28.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f))
                        Text("OR", modifier = Modifier.padding(horizontal = 16.dp), style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.Gray))
                        HorizontalDivider(modifier = Modifier.weight(1f))
                    }

                    // Email
                    Text("Email", style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1F2937)), modifier = Modifier.padding(bottom = 8.dp))
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
                            focusedBorderColor = Color(0xFF6366F1),
                            unfocusedBorderColor = Color.Transparent
                        ),
                        enabled = !isLoading
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Password
                    Text("Password", style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1F2937)), modifier = Modifier.padding(bottom = 8.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        trailingIcon = {
                            IconButton(onClick = { visibility = !visibility }) {
                                Icon(
                                    painter = if (visibility) painterResource(R.drawable.baseline_visibility_24)
                                    else painterResource(R.drawable.baseline_visibility_off_24),
                                    contentDescription = null
                                )
                            }
                        },
                        visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
                        shape = RoundedCornerShape(12.dp),
                        placeholder = { Text("********") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF3F4F6),
                            unfocusedContainerColor = Color(0xFFF3F4F6),
                            focusedBorderColor = Color(0xFF6366F1),
                            unfocusedBorderColor = Color.Transparent
                        ),
                        enabled = !isLoading
                    )

                    // ✅ Forgot Password uses AuthViewModel
                    Text(
                        "Forgot password?",
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF6366F1)),
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(top = 12.dp)
                            .clickable(enabled = !isLoading) {
                                if (email.isEmpty()) {
                                    Toast.makeText(context, "Please enter your email address", Toast.LENGTH_SHORT).show()
                                } else {
                                    authViewModel.sendPasswordReset(email) { success, message ->
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // ✅ Login Button uses AuthViewModel
                    Button(
                        onClick = {
                            if (email.isEmpty() || password.isEmpty()) {
                                Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            isLoading = true

                            authViewModel.login(email, password) { success, message ->
                                isLoading = false
                                if (success) {
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                                    val sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE)
                                    sharedPreferences.edit().putString("email", email).apply()

                                    // Check if admin email
                                    if (email.trim() == "admin@gmail.com") {
                                        context.startActivity(Intent(context, AdminDashboardActivity::class.java).apply {
                                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        })
                                    } else {
                                        val fullName = authViewModel.getCurrentUserName() ?: ""
                                        val intent = Intent(context, DashBoardActivity::class.java).apply {
                                            putExtra("email", email)
                                            putExtra("fullName", fullName)
                                        }
                                        context.startActivity(intent)
                                    }
                                    activity.finish()
                                } else {
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                        } else {
                            Text("Log In", style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                buildAnnotatedString {
                    withStyle(SpanStyle(color = Color.White.copy(alpha = 0.9f))) { append("Don't have an account? ") }
                    withStyle(SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) { append("Sign up") }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = !isLoading) {
                        context.startActivity(Intent(context, RegistrationActivity::class.java))
                        activity.finish()
                    }
                    .padding(vertical = 16.dp),
                style = TextStyle(textAlign = TextAlign.Center, fontSize = 15.sp)
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun SocialMediaCard(
    modifier: Modifier,
    image: Int,
    label: String,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    OutlinedCard(
        modifier = modifier,
        colors = CardDefaults.outlinedCardColors(containerColor = Color.Transparent),
        border = CardDefaults.outlinedCardBorder().copy(
            width = 1.5.dp,
            brush = Brush.linearGradient(colors = listOf(Color(0xFFE5E7EB), Color(0xFFD1D5DB)))
        ),
        shape = RoundedCornerShape(12.dp),
        onClick = onClick,
        enabled = enabled
    ) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Image(painter = painterResource(image), contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(label, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF374151)))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLogin() {
    TravelSafeTheme { LoginBody() }
}