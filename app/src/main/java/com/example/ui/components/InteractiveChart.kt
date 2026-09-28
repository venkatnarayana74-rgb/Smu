package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CandleStick
import com.example.ui.theme.TvGreen
import com.example.ui.theme.TvRed
import java.util.Locale

@Composable
fun InteractiveChart(
    candlesticks: List<CandleStick>,
    isCandlestickMode: Boolean,
    modifier: Modifier = Modifier
) {
    if (candlesticks.isEmpty()) return

    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f

    val minPrice = candlesticks.minOf { it.low }
    val maxPrice = candlesticks.maxOf { it.high }
    val priceRange = (maxPrice - minPrice).coerceAtLeast(0.01f)

    val gridColor = if (isDark) Color(0xFF2A2E39) else Color(0xFFE0E3EB)
    val crosshairColor = if (isDark) Color(0xFF787B86) else Color(0xFF9598A1)
    val textPrimary = MaterialTheme.colorScheme.onBackground

    val activeCandle = selectedIndex?.let { candlesticks.getOrNull(it) } ?: candlesticks.lastOrNull()

    Column(modifier = modifier.fillMaxWidth()) {
        // Active candle stats header
        if (activeCandle != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "O: ${String.format(Locale.US, "%.2f", activeCandle.open)}",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "H: ${String.format(Locale.US, "%.2f", activeCandle.high)}",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "L: ${String.format(Locale.US, "%.2f", activeCandle.low)}",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "C: ${String.format(Locale.US, "%.2f", activeCandle.close)}",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = if (activeCandle.close >= activeCandle.open) TvGreen else TvRed
                    )
                }
                Text(
                    text = activeCandle.timestamp,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(candlesticks) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                val step = size.width / candlesticks.size
                                val idx = (offset.x / step).toInt().coerceIn(0, candlesticks.size - 1)
                                selectedIndex = idx
                            },
                            onDrag = { change, _ ->
                                val step = size.width / candlesticks.size
                                val idx = (change.position.x / step).toInt().coerceIn(0, candlesticks.size - 1)
                                selectedIndex = idx
                            },
                            onDragEnd = { /* keep selected */ },
                            onDragCancel = { selectedIndex = null }
                        )
                    }
                    .pointerInput(candlesticks) {
                        detectTapGestures { offset ->
                            val step = size.width / candlesticks.size
                            val idx = (offset.x / step).toInt().coerceIn(0, candlesticks.size - 1)
                            selectedIndex = idx
                        }
                    }
            ) {
                val w = size.width
                val h = size.height

                // Draw 4 horizontal gridlines
                val gridLines = 4
                for (i in 0..gridLines) {
                    val y = h * (i.toFloat() / gridLines)
                    drawLine(
                        color = gridColor,
                        start = Offset(0f, y),
                        end = Offset(w, y),
                        strokeWidth = 1f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f))
                    )
                }

                val count = candlesticks.size
                val candleWidth = (w / count)

                if (isCandlestickMode) {
                    // Draw Candlesticks
                    candlesticks.forEachIndexed { index, candle ->
                        val isUp = candle.close >= candle.open
                        val color = if (isUp) TvGreen else TvRed
                        val centerX = index * candleWidth + (candleWidth / 2f)

                        val highY = h - ((candle.high - minPrice) / priceRange * h)
                        val lowY = h - ((candle.low - minPrice) / priceRange * h)
                        val openY = h - ((candle.open - minPrice) / priceRange * h)
                        val closeY = h - ((candle.close - minPrice) / priceRange * h)

                        // Wick line
                        drawLine(
                            color = color,
                            start = Offset(centerX, highY),
                            end = Offset(centerX, lowY),
                            strokeWidth = 2.5f
                        )

                        // Body rect
                        val topY = minOf(openY, closeY)
                        val bottomY = maxOf(openY, closeY)
                        val bodyHeight = (bottomY - topY).coerceAtLeast(3f)
                        val bodyW = (candleWidth * 0.7f).coerceIn(4f, 24f)

                        drawRect(
                            color = color,
                            topLeft = Offset(centerX - bodyW / 2f, topY),
                            size = androidx.compose.ui.geometry.Size(bodyW, bodyHeight)
                        )
                    }
                } else {
                    // Draw Area / Line chart
                    val linePath = Path()
                    val fillPath = Path()

                    val isUpTrend = candlesticks.last().close >= candlesticks.first().close
                    val trendColor = if (isUpTrend) TvGreen else TvRed

                    candlesticks.forEachIndexed { index, candle ->
                        val x = index * candleWidth + (candleWidth / 2f)
                        val y = h - ((candle.close - minPrice) / priceRange * h)

                        if (index == 0) {
                            linePath.moveTo(x, y)
                            fillPath.moveTo(x, h)
                            fillPath.lineTo(x, y)
                        } else {
                            val prevX = (index - 1) * candleWidth + (candleWidth / 2f)
                            val prevCandle = candlesticks[index - 1]
                            val prevY = h - ((prevCandle.close - minPrice) / priceRange * h)

                            val midX = (prevX + x) / 2f
                            linePath.cubicTo(midX, prevY, midX, y, x, y)
                            fillPath.cubicTo(midX, prevY, midX, y, x, y)
                        }

                        if (index == count - 1) {
                            fillPath.lineTo(x, h)
                            fillPath.close()
                        }
                    }

                    // Fill gradient
                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                trendColor.copy(alpha = 0.35f),
                                trendColor.copy(alpha = 0.02f)
                            ),
                            startY = 0f,
                            endY = h
                        )
                    )

                    // Line stroke
                    drawPath(
                        path = linePath,
                        color = trendColor,
                        style = Stroke(width = 4f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )
                }

                // Draw Crosshair if active
                selectedIndex?.let { idx ->
                    val candle = candlesticks.getOrNull(idx) ?: return@let
                    val crossX = idx * candleWidth + (candleWidth / 2f)
                    val crossY = h - ((candle.close - minPrice) / priceRange * h)

                    // Vertical dashed crosshair
                    drawLine(
                        color = crosshairColor,
                        start = Offset(crossX, 0f),
                        end = Offset(crossX, h),
                        strokeWidth = 1.5f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f))
                    )

                    // Horizontal dashed crosshair
                    drawLine(
                        color = crosshairColor,
                        start = Offset(0f, crossY),
                        end = Offset(w, crossY),
                        strokeWidth = 1.5f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f))
                    )

                    // Dot at intersection
                    drawCircle(
                        color = Color.White,
                        radius = 6f,
                        center = Offset(crossX, crossY)
                    )
                    drawCircle(
                        color = if (candle.close >= candle.open) TvGreen else TvRed,
                        radius = 4f,
                        center = Offset(crossX, crossY)
                    )
                }
            }
        }
    }
}
