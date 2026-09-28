package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
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
import com.example.model.MarketAsset
import com.example.model.ScreenerTab
import com.example.model.TechnicalRating
import com.example.ui.theme.*
import java.util.Locale

@Composable
fun MarketScreenerTable(
    assets: List<MarketAsset>,
    selectedTab: ScreenerTab,
    onSelectTab: (ScreenerTab) -> Unit,
    watchlistSymbols: Set<String>,
    onToggleWatchlist: (String) -> Unit,
    onSelectAsset: (MarketAsset) -> Unit,
    onViewAllClick: () -> Unit,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    val borderColor = MaterialTheme.colorScheme.outline
    val surfaceColor = MaterialTheme.colorScheme.surface
    val tableHeadBg = if (isDarkMode) TvDarkCard else TvTableHead
    val onSurface = MaterialTheme.colorScheme.onSurface

    // Pulsing animation for Real-time Data dot
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val dotAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Surface(
        color = surfaceColor,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Tabs Bar + Real-time indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = borderColor)
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Horizontal scrollable screener tabs
                Row(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ScreenerTab.entries.forEach { tab ->
                        val isSelected = tab == selectedTab
                        Column(
                            modifier = Modifier
                                .clickable { onSelectTab(tab) }
                                .padding(vertical = 10.dp)
                        ) {
                            Text(
                                text = tab.label,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                color = if (isSelected) TvBlue else TvGray
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(if (isSelected) 36.dp else 0.dp)
                                    .height(2.dp)
                                    .background(if (isSelected) TvBlue else Color.Transparent)
                            )
                        }
                    }
                }

                // Real-time pulse indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Real-time Data",
                        fontSize = 12.sp,
                        color = TvGray,
                        fontWeight = FontWeight.Medium
                    )
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(TvGreen.copy(alpha = dotAlpha))
                    )
                }
            }

            // Horizontally Scrollable Table for responsive widths
            val tableScrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(tableScrollState)
            ) {
                // Table Header Row
                Row(
                    modifier = Modifier
                        .width(820.dp)
                        .background(tableHeadBg)
                        .border(1.dp, borderColor)
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SYMBOL",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TvGray,
                        modifier = Modifier.width(220.dp)
                    )
                    Text(
                        text = "PRICE (USD)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TvGray,
                        modifier = Modifier.width(100.dp)
                    )
                    Text(
                        text = "CHANGE %",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TvGray,
                        modifier = Modifier.width(85.dp)
                    )
                    Text(
                        text = "CHANGE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TvGray,
                        modifier = Modifier.width(80.dp)
                    )
                    Text(
                        text = "HIGH",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TvGray,
                        modifier = Modifier.width(85.dp)
                    )
                    Text(
                        text = "LOW",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TvGray,
                        modifier = Modifier.width(85.dp)
                    )
                    Text(
                        text = "VOLUME",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TvGray,
                        modifier = Modifier.width(75.dp)
                    )
                    Text(
                        text = "TECHNICAL RATING",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TvGray,
                        modifier = Modifier.width(130.dp)
                    )
                }

                // Table Rows
                assets.forEach { asset ->
                    val isFavorite = watchlistSymbols.contains(asset.symbol)
                    val isPositive = asset.change >= 0

                    Row(
                        modifier = Modifier
                            .width(820.dp)
                            .border(width = 0.5.dp, color = borderColor)
                            .clickable { onSelectAsset(asset) }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Symbol Column (Star + Avatar + Name + Subtitle)
                        Row(
                            modifier = Modifier.width(220.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IconButton(
                                onClick = { onToggleWatchlist(asset.symbol) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                    contentDescription = "Favorite",
                                    tint = if (isFavorite) Color(0xFFFFB300) else TvGray.copy(alpha = 0.6f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            // Circular logo avatar
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(asset.logoColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = asset.logoLetter,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Column {
                                Text(
                                    text = asset.symbol,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = onSurface
                                )
                                Text(
                                    text = asset.name,
                                    fontSize = 11.sp,
                                    color = TvGray,
                                    maxLines = 1
                                )
                            }
                        }

                        // Price Column
                        Text(
                            text = String.format(Locale.US, "%,.2f", asset.price),
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = onSurface,
                            modifier = Modifier.width(100.dp)
                        )

                        // Change % Column
                        val sign = if (isPositive) "+" else ""
                        Text(
                            text = "$sign${String.format(Locale.US, "%.2f", asset.changePercent)}%",
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = if (isPositive) TvGreen else TvRed,
                            modifier = Modifier.width(85.dp)
                        )

                        // Change Column
                        Text(
                            text = "$sign${String.format(Locale.US, "%.2f", asset.change)}",
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Medium,
                            color = if (isPositive) TvGreen else TvRed,
                            modifier = Modifier.width(80.dp)
                        )

                        // High Column
                        Text(
                            text = String.format(Locale.US, "%,.2f", asset.high),
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            color = TvGray,
                            modifier = Modifier.width(85.dp)
                        )

                        // Low Column
                        Text(
                            text = String.format(Locale.US, "%,.2f", asset.low),
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            color = TvGray,
                            modifier = Modifier.width(85.dp)
                        )

                        // Volume Column
                        Text(
                            text = asset.volume,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            color = TvGray,
                            modifier = Modifier.width(75.dp)
                        )

                        // Technical Rating Badge Column
                        Box(
                            modifier = Modifier.width(130.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            TechnicalRatingBadge(rating = asset.technicalRating, isDarkMode = isDarkMode)
                        }
                    }
                }
            }

            // Table Bottom Footer Link: "View entire market screener >"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(tableHeadBg)
                    .border(width = 1.dp, color = borderColor)
                    .clickable { onViewAllClick() }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "View entire market screener",
                        color = TvBlue,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Expand screener",
                        tint = TvBlue,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TechnicalRatingBadge(
    rating: TechnicalRating,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    val (bg, textColor) = when (rating) {
        TechnicalRating.STRONG_BUY -> {
            if (isDarkMode) Pair(Color(0xFF0F3B32), Color(0xFF26A69A))
            else Pair(Color(0xFFD1F2E9), Color(0xFF065F46))
        }
        TechnicalRating.BUY -> {
            if (isDarkMode) Pair(Color(0xFF13322B), Color(0xFF34D399))
            else Pair(Color(0xFFE6F4F1), Color(0xFF047857))
        }
        TechnicalRating.NEUTRAL -> {
            if (isDarkMode) Pair(Color(0xFF2A2E39), Color(0xFF9598A1))
            else Pair(Color(0xFFF0F3FA), Color(0xFF4B5563))
        }
        TechnicalRating.SELL -> {
            if (isDarkMode) Pair(Color(0xFF3E1E24), Color(0xFFF87171))
            else Pair(Color(0xFFFEECEE), Color(0xFFB91C1C))
        }
        TechnicalRating.STRONG_SELL -> {
            if (isDarkMode) Pair(Color(0xFF4A1820), Color(0xFFEF4444))
            else Pair(Color(0xFFFCDADA), Color(0xFF991B1B))
        }
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(9999.dp))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 3.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = rating.label,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
