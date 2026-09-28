package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun TradingViewLogo(
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.onBackground
) {
    Canvas(modifier = modifier.size(32.dp, 26.dp)) {
        val w = size.width
        val h = size.height
        val scaleX = w / 36f
        val scaleY = h / 28f

        // Top horizontal bars
        drawRect(color = tint, topLeft = Offset(0f * scaleX, 0f * scaleY), size = Size(12f * scaleX, 4f * scaleY))
        drawRect(color = tint, topLeft = Offset(14f * scaleX, 0f * scaleY), size = Size(12f * scaleX, 4f * scaleY))
        drawRect(color = tint, topLeft = Offset(28f * scaleX, 0f * scaleY), size = Size(8f * scaleX, 4f * scaleY))

        // Vertical bars
        drawRect(color = tint, topLeft = Offset(0f * scaleX, 6f * scaleY), size = Size(4f * scaleX, 16f * scaleY))
        drawRect(color = tint, topLeft = Offset(8f * scaleX, 6f * scaleY), size = Size(4f * scaleX, 22f * scaleY))
        drawRect(color = tint, topLeft = Offset(22f * scaleX, 6f * scaleY), size = Size(4f * scaleX, 22f * scaleY))
        drawRect(color = tint, topLeft = Offset(32f * scaleX, 6f * scaleY), size = Size(4f * scaleX, 10f * scaleY))
    }
}

@Composable
fun TradingViewHeader(
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    onOpenSearch: () -> Unit,
    onOpenWatchlist: () -> Unit,
    watchlistCount: Int,
    onGetStartedClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = MaterialTheme.colorScheme.outline
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurface = MaterialTheme.colorScheme.onSurface
    val searchBg = if (isDarkMode) TvDarkCard else TvBgLight

    Surface(
        color = surfaceColor,
        modifier = modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Logo + Search
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TradingViewLogo(
                    modifier = Modifier.clickable { /* Reset or home */ }
                )

                // Search Pill
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(9999.dp))
                        .background(searchBg)
                        .clickable { onOpenSearch() }
                        .padding(horizontal = 12.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = TvGray,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Search (Ctrl+K)",
                        fontSize = 13.sp,
                        color = TvGray,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            // Right: Watchlist + DarkMode + Locale + Profile + CTA
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Watchlist Star with Badge
                Box(contentAlignment = Alignment.TopEnd) {
                    IconButton(
                        onClick = onOpenWatchlist,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (watchlistCount > 0) Icons.Filled.Star else Icons.Outlined.StarBorder,
                            contentDescription = "Watchlist",
                            tint = if (watchlistCount > 0) Color(0xFFFFB300) else onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    if (watchlistCount > 0) {
                        Box(
                            modifier = Modifier
                                .offset(x = (-2).dp, y = 2.dp)
                                .clip(CircleShape)
                                .background(TvBlue)
                                .size(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = watchlistCount.toString(),
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Dark / Light Mode Toggle
                IconButton(
                    onClick = onToggleDarkMode,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = "Toggle Theme",
                        tint = onSurface,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Locale Pill
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { }
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "Language",
                        tint = onSurface,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "EN",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = onSurface
                    )
                }

                // Get Started Gradient CTA Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(9999.dp))
                        .background(TvButtonGradient)
                        .clickable { onGetStartedClick() }
                        .padding(horizontal = 14.dp, vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Get started",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
