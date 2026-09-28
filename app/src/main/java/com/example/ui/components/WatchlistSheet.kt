package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MarketAsset
import com.example.ui.theme.TvGray
import com.example.ui.theme.TvGreen
import com.example.ui.theme.TvRed
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistSheet(
    watchlistAssets: List<MarketAsset>,
    onSelectAsset: (MarketAsset) -> Unit,
    onRemoveFromWatchlist: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurface = MaterialTheme.colorScheme.onSurface

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = surfaceColor,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.65f)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "My Watchlist",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = onSurface
                    )
                    Text(
                        text = "${watchlistAssets.size} symbols tracked",
                        fontSize = 12.sp,
                        color = TvGray
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (watchlistAssets.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No saved symbols yet",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tap the star icon on any asset in the table to add it here.",
                            fontSize = 12.sp,
                            color = TvGray
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(watchlistAssets) { asset ->
                        val isPositive = asset.change >= 0
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
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
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(CircleShape)
                                            .background(asset.logoColor),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = asset.logoLetter,
                                            color = Color.White,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Column {
                                        Text(
                                            text = asset.symbol,
                                            fontSize = 14.sp,
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

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "$${String.format(Locale.US, "%,.2f", asset.price)}",
                                            fontSize = 14.sp,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            color = onSurface
                                        )
                                        val sign = if (isPositive) "+" else ""
                                        Text(
                                            text = "$sign${String.format(Locale.US, "%.2f", asset.changePercent)}%",
                                            fontSize = 12.sp,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (isPositive) TvGreen else TvRed
                                        )
                                    }

                                    IconButton(
                                        onClick = { onRemoveFromWatchlist(asset.symbol) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.DeleteOutline,
                                            contentDescription = "Remove",
                                            tint = TvGray,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
