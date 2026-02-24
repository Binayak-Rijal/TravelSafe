package com.example.travelsafe

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.travelsafe.ui.theme.TravelSafeTheme
import com.example.travelsafe.view.SearchScreen

class SearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TravelSafeTheme {
                SearchScreen(
                    onBackClick = { finish() },
                    onSearchClick = { from, to, passengers ->
                        val intent = Intent(this, ResultsActivity::class.java).apply {
                            putExtra("from", from)
                            putExtra("to", to)
                            putExtra("passengers", passengers)
                        }
                        startActivity(intent)
                    }
                )
            }
        }
    }
}