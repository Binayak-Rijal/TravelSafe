package com.example.travelsafe.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.travelsafe.ui.theme.AppColors
import com.example.travelsafe.ui.theme.AppType

@Composable
fun SectionTitle(title: String, subtitle: String? = null) {
    Column(Modifier.padding(horizontal = 22.dp)) {
        Text(title, style = AppType.headlineMedium.copy(color = AppColors.TextPrimary))
        if (subtitle != null)
            Text(subtitle, style = AppType.bodyMedium.copy(color = AppColors.TextSecondary))
    }
}

