package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TvGray

@Composable
fun TradingViewFooter(
    modifier: Modifier = Modifier
) {
    val borderColor = MaterialTheme.colorScheme.outline
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurface = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(surfaceColor)
            .border(width = 1.dp, color = borderColor)
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "© 2026 TradingView",
                fontSize = 12.sp,
                color = TvGray,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Terms of use",
                fontSize = 12.sp,
                color = TvGray,
                modifier = Modifier.clickable { }
            )
            Text(
                text = "Privacy policy",
                fontSize = 12.sp,
                color = TvGray,
                modifier = Modifier.clickable { }
            )
            Text(
                text = "Cookies",
                fontSize = 12.sp,
                color = TvGray,
                modifier = Modifier.clickable { }
            )
        }

        Text(
            text = "Quotes and data provided by leading financial exchanges.",
            fontSize = 11.sp,
            color = TvGray.copy(alpha = 0.8f)
        )
    }
}
