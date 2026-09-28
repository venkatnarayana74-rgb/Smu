package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.model.ChartTimeframe
import com.example.model.MarketAsset
import com.example.model.TechnicalRating
import com.example.ui.theme.*
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetDetailSheet(
    asset: MarketAsset,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onDismiss: () -> Unit,
    onTradeClick: (isBuy: Boolean) -> Unit,
    isDarkMode: Boolean
) {
    var selectedTimeframe by remember { mutableStateOf(ChartTimeframe.T_1D) }
    var isCandlestickMode by remember { mutableStateOf(false) }

    val borderColor = MaterialTheme.colorScheme.outline
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurface = MaterialTheme.colorScheme.onSurface
    val isPositive = asset.change >= 0

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = surfaceColor,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Header Row: Logo, Symbol, Name, Close & Star
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(asset.logoColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = asset.logoLetter,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = asset.symbol,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = onSurface
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (isDarkMode) TvDarkCard else TvBgLight)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = asset.category.label,
                                    fontSize = 10.sp,
                                    color = TvGray,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        Text(
                            text = asset.name,
                            fontSize = 13.sp,
                            color = TvGray
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isDarkMode) TvDarkCard else TvBgLight)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color(0xFFFFB300) else onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isDarkMode) TvDarkCard else TvBgLight)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Price & Change Large Display
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "$${String.format(Locale.US, "%,.2f", asset.price)}",
                        fontSize = 28.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = onSurface
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val sign = if (isPositive) "+" else ""
                        Text(
                            text = "$sign${String.format(Locale.US, "%.2f", asset.change)} ($sign${String.format(Locale.US, "%.2f", asset.changePercent)}%)",
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = if (isPositive) TvGreen else TvRed
                        )
                        Text(
                            text = "Today",
                            fontSize = 12.sp,
                            color = TvGray
                        )
                    }
                }

                // Chart mode toggle (Area vs Candlesticks)
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isDarkMode) TvDarkCard else TvBgLight)
                        .padding(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (!isCandlestickMode) TvBlue else Color.Transparent)
                            .clickable { isCandlestickMode = false }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShowChart,
                            contentDescription = "Line chart",
                            tint = if (!isCandlestickMode) Color.White else TvGray,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isCandlestickMode) TvBlue else Color.Transparent)
                            .clickable { isCandlestickMode = true }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = "Candlestick chart",
                            tint = if (isCandlestickMode) Color.White else TvGray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Timeframe Selector: 1D, 5D, 1M, 6M, 1Y, ALL
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ChartTimeframe.entries.forEach { tf ->
                    val isSelected = tf == selectedTimeframe
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (isSelected) {
                                    if (isDarkMode) TvBlue.copy(alpha = 0.25f) else TvLightBlue
                                } else Color.Transparent
                            )
                            .clickable { selectedTimeframe = tf }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = tf.label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) TvBlue else TvGray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Interactive Chart
            InteractiveChart(
                candlesticks = asset.candlesticks,
                isCandlestickMode = isCandlestickMode,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Technical Analysis Speedometers Section
            Text(
                text = "Technical Analysis",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = if (isDarkMode) TvDarkCard else TvBgLight,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TechnicalGauge(
                        title = "Oscillators",
                        rating = asset.oscillatorRating,
                        scorePercent = if (asset.oscillatorRating.contains("Buy")) 0.75f else 0.5f
                    )
                    TechnicalGauge(
                        title = "Summary",
                        rating = asset.technicalRating.label,
                        scorePercent = when (asset.technicalRating) {
                            TechnicalRating.STRONG_BUY -> 0.9f
                            TechnicalRating.BUY -> 0.75f
                            TechnicalRating.NEUTRAL -> 0.5f
                            TechnicalRating.SELL -> 0.25f
                            TechnicalRating.STRONG_SELL -> 0.1f
                        }
                    )
                    TechnicalGauge(
                        title = "Moving Avg",
                        rating = asset.movingAverageRating,
                        scorePercent = if (asset.movingAverageRating.contains("Buy")) 0.85f else 0.5f
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Key Statistics Grid & Ranges
            Text(
                text = "Key Statistics",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = onSurface
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Day Range Bar
            RangeBar(
                title = "Day Range",
                lowVal = asset.low,
                highVal = asset.high,
                currentVal = asset.price,
                isDarkMode = isDarkMode
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 52-Week Range Bar
            RangeBar(
                title = "52-Week Range",
                lowVal = asset.week52Low,
                highVal = asset.week52High,
                currentVal = asset.price,
                isDarkMode = isDarkMode
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Stats 2x2 Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatBox(label = "Market Cap", value = asset.marketCap, modifier = Modifier.weight(1f), isDarkMode = isDarkMode)
                StatBox(label = "Volume", value = asset.volume, modifier = Modifier.weight(1f), isDarkMode = isDarkMode)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatBox(label = "Open", value = "$${asset.openPrice}", modifier = Modifier.weight(1f), isDarkMode = isDarkMode)
                StatBox(label = "P/E Ratio", value = asset.peRatio?.toString() ?: "N/A", modifier = Modifier.weight(1f), isDarkMode = isDarkMode)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Company / Asset Profile description
            Text(
                text = "About ${asset.name}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = asset.description,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = TvGray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Trade Simulation Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { onTradeClick(true) },
                    colors = ButtonDefaults.buttonColors(containerColor = TvGreen),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text(
                        text = "Trade: Buy ${asset.symbol}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Button(
                    onClick = { onTradeClick(false) },
                    colors = ButtonDefaults.buttonColors(containerColor = TvRed),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text(
                        text = "Trade: Sell ${asset.symbol}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun StatBox(
    label: String,
    value: String,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        color = if (isDarkMode) TvDarkCard else TvBgLight,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
            Text(text = label, fontSize = 11.sp, color = TvGray)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun RangeBar(
    title: String,
    lowVal: Double,
    highVal: Double,
    currentVal: Double,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    val totalRange = (highVal - lowVal).coerceAtLeast(0.001)
    val fraction = ((currentVal - lowVal) / totalRange).toFloat().coerceIn(0f, 1f)

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, fontSize = 12.sp, color = TvGray, fontWeight = FontWeight.Medium)
            Text(
                text = "Current: $${String.format(Locale.US, "%,.2f", currentVal)}",
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(6.dp))

        // Progress line with indicator
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(if (isDarkMode) TvDarkCard else Color(0xFFE0E3EB))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction)
                    .background(TvBlue)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$${String.format(Locale.US, "%,.2f", lowVal)}",
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                color = TvGray
            )
            Text(
                text = "$${String.format(Locale.US, "%,.2f", highVal)}",
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                color = TvGray
            )
        }
    }
}
