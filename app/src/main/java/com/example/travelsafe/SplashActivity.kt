package com.example.travelsafe

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelsafe.ui.theme.TravelSafeTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {


    private val ADMIN_EMAIL = "admin@gmail.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TravelSafeTheme {
                SplashBody(
                    onNavigate = { isLoggedIn, displayName, isAdmin ->
                        when {
                            // ✅ Admin goes to Admin Dashboard
                            isLoggedIn && isAdmin -> {
                                startActivity(Intent(this, AdminDashboardActivity::class.java))
                            }
                            // ✅ Normal user goes to User Dashboard
                            isLoggedIn && !isAdmin -> {
                                val intent = Intent(this, DashBoardActivity::class.java).apply {
                                    putExtra("fullName", displayName)
                                }
                                startActivity(intent)
                            }
                            // ✅ Not logged in goes to Login
                            else -> {
                                startActivity(Intent(this, LoginActivity::class.java))
                            }
                        }
                        finish()
                    },
                    adminEmail = ADMIN_EMAIL
                )
            }
        }
    }
}

@Composable
fun SplashBody(
    onNavigate: (Boolean, String, Boolean) -> Unit = { _, _, _ -> },
    adminEmail: String = "admin@gmail.com"
) {
    val auth = remember { FirebaseAuth.getInstance() }

    LaunchedEffect(Unit) {
        delay(2500)
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // ✅ Check if the logged in user is admin
            val isAdmin = currentUser.email == adminEmail
            onNavigate(true, currentUser.displayName ?: "User", isAdmin)
        } else {
            onNavigate(false, "", false)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.splash_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(100.dp))

            Text(
                text = "Explore\nthe World",
                style = TextStyle(
                    fontSize = 70.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 75.sp
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Manage your trips with\nour app",
                style = TextStyle(
                    fontSize = 25.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 30.sp
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Let's start our journey",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}