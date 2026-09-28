package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MarketCategory
import com.example.ui.theme.TvBgLight
import com.example.ui.theme.TvDark
import com.example.ui.theme.TvDarkSurface

@Composable
fun MarketCategoryNav(
    selectedCategory: MarketCategory,
    onSelectCategory: (MarketCategory) -> Unit,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    val borderColor = MaterialTheme.colorScheme.outline
    val surfaceColor = MaterialTheme.colorScheme.surface
    val activeBg = if (isDarkMode) Color.White else TvDark
    val activeText = if (isDarkMode) TvDark else Color.White
    val inactiveText = MaterialTheme.colorScheme.onSurface
    val inactiveHoverBg = if (isDarkMode) TvDarkSurface else TvBgLight

    Surface(
        color = surfaceColor,
        modifier = modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MarketCategory.entries.forEach { category ->
                val isSelected = category == selectedCategory
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(9999.dp))
                        .background(if (isSelected) activeBg else Color.Transparent)
                        .clickable { onSelectCategory(category) }
                        .padding(horizontal = 14.dp, vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category.label,
                        color = if (isSelected) activeText else inactiveText,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                    )
                }
            }
        }
    }
}
