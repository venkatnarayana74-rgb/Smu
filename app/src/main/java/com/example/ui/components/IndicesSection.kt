package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MarketIndex
import com.example.ui.theme.*
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun IndicesSection(
    indices: List<MarketIndex>,
    onSelectIndex: (MarketIndex) -> Unit,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val borderColor = MaterialTheme.colorScheme.outline
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurface = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Section Header: "Indices >" + Arrows
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { /* open all indices */ }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Indices",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = onSurface
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "View all indices",
                    tint = onSurface,
                    modifier = Modifier.size(22.dp)
                )
            }

            // Left / Right arrow navigation buttons
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .border(1.dp, borderColor, CircleShape)
                        .clickable(enabled = scrollState.value > 0) {
                            coroutineScope.launch {
                                scrollState.animateScrollTo((scrollState.value - 600).coerceAtLeast(0))
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = "Previous indices",
                        tint = if (scrollState.value > 0) onSurface else TvGray.copy(alpha = 0.4f),
                        modifier = Modifier.size(18.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .border(1.dp, borderColor, CircleShape)
                        .clickable(enabled = scrollState.value < scrollState.maxValue) {
                            coroutineScope.launch {
                                scrollState.animateScrollTo(
                                    (scrollState.value + 600).coerceAtMost(scrollState.maxValue)
                                )
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Next indices",
                        tint = if (scrollState.value < scrollState.maxValue) onSurface else TvGray.copy(alpha = 0.4f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Horizontal Row of Indices Cards
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            indices.forEach { index ->
                IndexCard(
                    index = index,
                    isDarkMode = isDarkMode,
                    onClick = { onSelectIndex(index) }
                )
            }
        }
    }
}

@Composable
fun IndexCard(
    index: MarketIndex,
    isDarkMode: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = MaterialTheme.colorScheme.outline
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurface = MaterialTheme.colorScheme.onSurface
    val isPositive = index.change >= 0

    val badgeBg = if (isPositive) {
        if (isDarkMode) TvGreenDarkBg else TvLightGreen
    } else {
        if (isDarkMode) TvRedDarkBg else TvLightRed
    }

    val badgeText = if (isPositive) TvGreen else TvRed

    Surface(
        color = surfaceColor,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .width(260.dp)
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Top: Circle Badge + Name/Symbol + Sparkline
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Circle Badge
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(index.badgeColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = index.badgeText,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Column {
                        Text(
                            text = index.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = onSurface
                        )
                        Text(
                            text = index.symbol,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = TvGray
                        )
                    }
                }

                // Sparkline
                Box(
                    modifier = Modifier
                        .width(76.dp)
                        .height(26.dp)
                ) {
                    SparklineChart(
                        points = index.sparkline,
                        isPositive = isPositive,
                        strokeWidth = 3.5f
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Bottom: Price + Gain/Loss Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = String.format(Locale.US, "%,.2f", index.price),
                    fontSize = 17.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = onSurface
                )

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(badgeBg)
                        .padding(horizontal = 6.dp, vertical = 3.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val sign = if (isPositive) "+" else ""
                    Text(
                        text = "$sign${String.format(Locale.US, "%.2f", index.change)}",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.SemiBold,
                        color = badgeText
                    )
                    Text(
                        text = "($sign${String.format(Locale.US, "%.2f", index.changePercent)}%)",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.SemiBold,
                        color = badgeText
                    )
                }
            }
        }
    }
}
