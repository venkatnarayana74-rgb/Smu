package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.TvBlue
import com.example.ui.theme.TvButtonGradient
import com.example.ui.theme.TvGray
import com.example.ui.theme.TvGreen

@Composable
fun GetStartedDialog(
    onDismiss: () -> Unit,
    isDarkMode: Boolean
) {
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurface = MaterialTheme.colorScheme.onSurface
    val borderColor = MaterialTheme.colorScheme.outline

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            color = surfaceColor,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, borderColor, RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TradingViewLogo()
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = onSurface)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Look first / Then leap.",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "The world's most popular financial charting and social network for traders and investors.",
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = TvGray
                )

                Spacer(modifier = Modifier.height(20.dp))

                val features = listOf(
                    "Real-time quotes from 150+ global exchanges",
                    "Advanced technical indicators & multi-timeframe charts",
                    "Stock, Crypto & Forex screener with custom filters",
                    "Interactive simulated paper trading"
                )

                features.forEach { feature ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(TvGreen.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = TvGreen,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                        Text(
                            text = feature,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(TvButtonGradient)
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        elevation = null
                    ) {
                        Text(
                            text = "Start Exploring Free",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
