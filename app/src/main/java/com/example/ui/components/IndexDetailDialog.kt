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
import androidx.compose.material.icons.filled.Close
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
import com.example.model.CandleStick
import com.example.model.ChartTimeframe
import com.example.model.MarketAsset
import com.example.model.MarketIndex
import com.example.ui.theme.*
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndexDetailDialog(
    index: MarketIndex,
    allAssets: List<MarketAsset>,
    onSelectAsset: (MarketAsset) -> Unit,
    onDismiss: () -> Unit,
    isDarkMode: Boolean
) {
    var selectedTimeframe by remember { mutableStateOf(ChartTimeframe.T_1D) }
    val isPositive = index.change >= 0
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurface = MaterialTheme.colorScheme.onSurface
    val borderColor = MaterialTheme.colorScheme.outline

    // Generate sample candle sticks based on index price
    val indexCandles = remember(index) {
        val base = index.price.toFloat()
        val count = 12
        val list = mutableListOf<CandleStick>()
        var curr = (base - index.change.toFloat()).coerceAtLeast(100f)
        val delta = index.change.toFloat() / count
        val times = listOf("09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00")
        for (i in 0 until count) {
            val open = curr
            val close = curr + delta + (if (i % 2 == 0) 10f else -5f)
            val high = maxOf(open, close) + 8f
            val low = minOf(open, close) - 6f
            list.add(CandleStick(times.getOrElse(i) { "$i:00" }, open, high, low, close))
            curr = close
        }
        list
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = surfaceColor,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Header
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
                            .background(index.badgeColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = index.badgeText,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = index.name,
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
                                    text = index.region.label,
                                    fontSize = 10.sp,
                                    color = TvGray,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        Text(
                            text = "${index.symbol} · Benchmark Index",
                            fontSize = 12.sp,
                            color = TvGray
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isDarkMode) TvDarkCard else TvBgLight)
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = onSurface)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Price & Change
            Text(
                text = String.format(Locale.US, "%,.2f", index.price),
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
                    text = "$sign${String.format(Locale.US, "%.2f", index.change)} ($sign${String.format(Locale.US, "%.2f", index.changePercent)}%)",
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = if (isPositive) TvGreen else TvRed
                )
                Text(text = "Today", fontSize = 12.sp, color = TvGray)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Timeframe Selector
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

            // Chart
            InteractiveChart(
                candlesticks = indexCandles,
                isCandlestickMode = false,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Index Constituents / Movers
            Text(
                text = "Key Constituents",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = onSurface
            )
            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                allAssets.take(4).forEach { asset ->
                    val isAssetUp = asset.change >= 0
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isDarkMode) TvDarkCard else TvBgLight,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelectAsset(asset)
                                onDismiss()
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(asset.logoColor),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = asset.logoLetter, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                                Column {
                                    Text(text = asset.symbol, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = onSurface)
                                    Text(text = asset.name, fontSize = 11.sp, color = TvGray)
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "$${String.format(Locale.US, "%,.2f", asset.price)}",
                                    fontSize = 13.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = onSurface
                                )
                                val sign = if (isAssetUp) "+" else ""
                                Text(
                                    text = "$sign${String.format(Locale.US, "%.2f", asset.changePercent)}%",
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isAssetUp) TvGreen else TvRed
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
