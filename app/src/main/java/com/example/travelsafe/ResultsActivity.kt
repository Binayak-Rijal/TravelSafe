package com.example.travelsafe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.travelsafe.ui.theme.TravelSafeTheme
import com.example.travelsafe.view.ResultsScreen

class ResultsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val from = intent.getStringExtra("from") ?: "New York"
        val to = intent.getStringExtra("to") ?: "Los Angeles"
        val passengers = intent.getIntExtra("passengers", 1)

        setContent {
            TravelSafeTheme {
                ResultsScreen(
                    from = from,
                    to = to,
                    passengers = passengers,
                    onBackClick = { finish() }
                )
            }
        }
    }
}