package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TvGreen
import com.example.ui.theme.TvRed
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun TechnicalGauge(
    title: String,
    rating: String,
    scorePercent: Float, // 0.0 (Strong Sell) to 1.0 (Strong Buy), 0.5 is Neutral
    modifier: Modifier = Modifier
) {
    val ratingColor = when {
        scorePercent >= 0.7f -> TvGreen
        scorePercent >= 0.55f -> TvGreen.copy(alpha = 0.8f)
        scorePercent >= 0.45f -> Color(0xFF787B86)
        scorePercent >= 0.3f -> TvRed.copy(alpha = 0.8f)
        else -> TvRed
    }

    Column(
        modifier = modifier.width(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .width(80.dp)
                .height(44.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height * 2
                val arcStroke = 6.dp.toPx()

                // Draw background arc (180 degrees from 180 to 360)
                // Red segment (left)
                drawArc(
                    color = TvRed.copy(alpha = 0.4f),
                    startAngle = 180f,
                    sweepAngle = 60f,
                    useCenter = false,
                    topLeft = Offset(arcStroke / 2, arcStroke / 2),
                    size = Size(w - arcStroke, h - arcStroke),
                    style = Stroke(width = arcStroke, cap = StrokeCap.Butt)
                )

                // Neutral segment (middle)
                drawArc(
                    color = Color(0xFF9598A1).copy(alpha = 0.4f),
                    startAngle = 240f,
                    sweepAngle = 60f,
                    useCenter = false,
                    topLeft = Offset(arcStroke / 2, arcStroke / 2),
                    size = Size(w - arcStroke, h - arcStroke),
                    style = Stroke(width = arcStroke, cap = StrokeCap.Butt)
                )

                // Green segment (right)
                drawArc(
                    color = TvGreen.copy(alpha = 0.4f),
                    startAngle = 300f,
                    sweepAngle = 60f,
                    useCenter = false,
                    topLeft = Offset(arcStroke / 2, arcStroke / 2),
                    size = Size(w - arcStroke, h - arcStroke),
                    style = Stroke(width = arcStroke, cap = StrokeCap.Butt)
                )

                // Needle
                val needleAngle = 180f + (scorePercent.coerceIn(0f, 1f) * 180f)
                val rad = Math.toRadians(needleAngle.toDouble())
                val needleLength = (w / 2) - 8.dp.toPx()
                val centerX = w / 2
                val centerY = size.height

                val endX = centerX + (needleLength * cos(rad)).toFloat()
                val endY = centerY + (needleLength * sin(rad)).toFloat()

                drawLine(
                    color = ratingColor,
                    start = Offset(centerX, centerY),
                    end = Offset(endX, endY),
                    strokeWidth = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )

                // Center pivot dot
                drawCircle(
                    color = ratingColor,
                    radius = 4.dp.toPx(),
                    center = Offset(centerX, centerY)
                )
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = rating,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = ratingColor
        )
    }
}
