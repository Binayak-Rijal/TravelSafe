package com.example.travelsafe.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ── TravelSafe Design System ─────────────────────────────────────────────────
// Luxury travel aesthetic: deep navy + warm gold, crisp whites, soft shadows

object AppColors {
    // Primary Palette
    val Navy        = Color(0xFF0D1B2A)
    val NavyMid     = Color(0xFF1A2F45)
    val NavyLight   = Color(0xFF243B55)

    // Accent
    val Gold        = Color(0xFFF0A500)
    val GoldLight   = Color(0xFFFFBF47)
    val GoldSoft    = Color(0xFFFFF0CC)

    // Backgrounds
    val BgPrimary   = Color(0xFFF6F4F0)
    val BgCard      = Color(0xFFFFFFFF)
    val BgSubtle    = Color(0xFFF0EDE8)

    // Text
    val TextPrimary   = Color(0xFF0D1B2A)
    val TextSecondary = Color(0xFF5C6C7C)
    val TextMuted     = Color(0xFFABB5BE)
    val TextInverse   = Color(0xFFFFFFFF)

    // Status
    val StatusPending   = Color(0xFFF59E0B)
    val StatusConfirmed = Color(0xFF10B981)
    val StatusCancelled = Color(0xFFEF4444)

    // Category Colors
    val CatBeach    = Color(0xFF0EA5E9)
    val CatMountain = Color(0xFF10B981)
    val CatCulture  = Color(0xFFF59E0B)
    val CatFood     = Color(0xFFEF4444)
    val CatGeneral  = Color(0xFF8B5CF6)

    // Divider
    val Divider     = Color(0xFFE8E4DF)
}

object AppType {
    val displayLarge  = TextStyle(fontSize = 34.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = (-1.0).sp, lineHeight = 40.sp)
    val displayMedium = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.5).sp, lineHeight = 32.sp)
    val headlineLarge = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.3).sp)
    val headlineMedium= TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.2).sp)
    val titleLarge    = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    val titleMedium   = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    val bodyLarge     = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Normal, lineHeight = 22.sp)
    val bodyMedium    = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Normal, lineHeight = 20.sp)
    val labelLarge    = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.3.sp)
    val labelSmall    = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.4.sp)
}