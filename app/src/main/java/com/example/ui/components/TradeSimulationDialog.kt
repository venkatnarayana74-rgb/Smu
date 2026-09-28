package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
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
import androidx.compose.ui.window.Dialog
import com.example.model.MarketAsset
import com.example.ui.theme.*
import java.util.Locale

@Composable
fun TradeSimulationDialog(
    asset: MarketAsset,
    initialIsBuy: Boolean,
    onDismiss: () -> Unit,
    isDarkMode: Boolean
) {
    var isBuy by remember { mutableStateOf(initialIsBuy) }
    var quantityText by remember { mutableStateOf("10") }
    var isExecuted by remember { mutableStateOf(false) }

    val quantity = quantityText.toDoubleOrNull() ?: 0.0
    val totalCost = quantity * asset.price

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
                    .padding(20.dp)
            ) {
                if (isExecuted) {
                    // Success View
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = TvGreen,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Order Executed!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${if (isBuy) "Bought" else "Sold"} $quantityText ${asset.symbol} @ $${String.format(Locale.US, "%,.2f", asset.price)}",
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                            color = TvGray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Total: $${String.format(Locale.US, "%,.2f", totalCost)}",
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = onSurface
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(containerColor = TvBlue),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Done", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Simulated Order",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = onSurface
                        )
                        IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = onSurface)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Buy / Sell Segmented Tab
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isDarkMode) TvDarkCard else TvBgLight)
                            .padding(3.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isBuy) TvGreen else Color.Transparent)
                                .clickable { isBuy = true }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Buy",
                                fontWeight = FontWeight.Bold,
                                color = if (isBuy) Color.White else TvGray
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (!isBuy) TvRed else Color.Transparent)
                                .clickable { isBuy = false }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Sell",
                                fontWeight = FontWeight.Bold,
                                color = if (!isBuy) Color.White else TvGray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Asset summary
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = asset.symbol, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = onSurface)
                            Text(text = asset.name, fontSize = 12.sp, color = TvGray)
                        }
                        Text(
                            text = "$${String.format(Locale.US, "%,.2f", asset.price)}",
                            fontSize = 16.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Quantity Input
                    Text(text = "Shares / Units", fontSize = 12.sp, color = TvGray, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = quantityText,
                        onValueChange = { quantityText = it },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TvBlue,
                            unfocusedBorderColor = borderColor,
                            focusedTextColor = onSurface,
                            unfocusedTextColor = onSurface
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Total Calculation
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Estimated Total:", fontSize = 14.sp, color = TvGray)
                        Text(
                            text = "$${String.format(Locale.US, "%,.2f", totalCost)}",
                            fontSize = 16.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Execute Button
                    Button(
                        onClick = { isExecuted = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isBuy) TvGreen else TvRed
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(
                            text = if (isBuy) "Place Buy Order" else "Place Sell Order",
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
