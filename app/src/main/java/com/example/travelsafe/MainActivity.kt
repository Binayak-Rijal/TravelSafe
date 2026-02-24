package com.example.travelsafe

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.travelsafe.ui.theme.TravelSafeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Launch SplashActivity
        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
        finish()
    }
}