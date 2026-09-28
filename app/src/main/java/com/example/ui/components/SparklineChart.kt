package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.TvGreen
import com.example.ui.theme.TvRed

@Composable
fun SparklineChart(
    points: List<Float>,
    isPositive: Boolean,
    modifier: Modifier = Modifier,
    strokeWidth: Float = 4f,
    showFill: Boolean = false
) {
    if (points.isEmpty()) return

    val lineColor = if (isPositive) TvGreen else TvRed

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val minVal = points.minOrNull() ?: 0f
        val maxVal = points.maxOrNull() ?: 1f
        val range = (maxVal - minVal).coerceAtLeast(0.001f)

        // Padding to ensure stroke isn't clipped
        val verticalPadding = 4f
        val effectiveHeight = height - (verticalPadding * 2)

        val path = Path()
        val fillPath = Path()

        val stepX = if (points.size > 1) width / (points.size - 1) else width

        points.forEachIndexed { index, value ->
            val normY = 1f - ((value - minVal) / range)
            val x = index * stepX
            val y = verticalPadding + (normY * effectiveHeight)

            if (index == 0) {
                path.moveTo(x, y)
                fillPath.moveTo(x, height)
                fillPath.lineTo(x, y)
            } else {
                val prevVal = points[index - 1]
                val prevNormY = 1f - ((prevVal - minVal) / range)
                val prevX = (index - 1) * stepX
                val prevY = verticalPadding + (prevNormY * effectiveHeight)

                // Smooth cubic bezier
                val cX1 = prevX + (stepX / 2f)
                val cY1 = prevY
                val cX2 = prevX + (stepX / 2f)
                val cY2 = y
                path.cubicTo(cX1, cY1, cX2, cY2, x, y)
                fillPath.cubicTo(cX1, cY1, cX2, cY2, x, y)
            }

            if (index == points.size - 1) {
                fillPath.lineTo(x, height)
                fillPath.close()
            }
        }

        if (showFill) {
            val fillBrush = Brush.verticalGradient(
                colors = listOf(
                    lineColor.copy(alpha = 0.25f),
                    lineColor.copy(alpha = 0.02f)
                ),
                startY = 0f,
                endY = height
            )
            drawPath(
                path = fillPath,
                brush = fillBrush
            )
        }

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}
