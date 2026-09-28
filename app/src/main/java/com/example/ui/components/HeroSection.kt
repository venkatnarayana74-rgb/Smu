package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MarketRegion
import com.example.ui.theme.*

@Composable
fun HeroSection(
    selectedRegion: MarketRegion,
    onSelectRegion: (MarketRegion) -> Unit,
    onDropdownClick: () -> Unit,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    val activeBg = if (isDarkMode) TvBlue.copy(alpha = 0.2f) else TvLightBlue
    val activeText = TvBlue
    val inactiveText = TvGray
    val onSurface = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 28.dp, bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Main Headline with chevron
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { onDropdownClick() }
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Markets, everywhere",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = onSurface,
                letterSpacing = (-1).sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(6.dp))
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (isDarkMode) TvDarkSurface else TvBgLight)
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Select Market Type",
                    tint = onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Region selector chips
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MarketRegion.entries.forEach { region ->
                val isSelected = region == selectedRegion
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(9999.dp))
                        .background(if (isSelected) activeBg else Color.Transparent)
                        .clickable { onSelectRegion(region) }
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = region.label,
                        color = if (isSelected) activeText else inactiveText,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }
    }
}
