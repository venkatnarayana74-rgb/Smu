package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
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
import com.example.model.MarketCategory
import com.example.ui.theme.*
import java.util.Locale

@Composable
fun SearchDialog(
    assets: List<MarketAsset>,
    onSelectAsset: (MarketAsset) -> Unit,
    onDismiss: () -> Unit,
    isDarkMode: Boolean
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf<MarketCategory?>(null) }

    val filteredAssets = remember(searchQuery, selectedFilter, assets) {
        assets.filter { asset ->
            val matchesCategory = selectedFilter == null || asset.category == selectedFilter
            val matchesQuery = searchQuery.isBlank() ||
                    asset.symbol.contains(searchQuery, ignoreCase = true) ||
                    asset.name.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }

    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurface = MaterialTheme.colorScheme.onSurface
    val borderColor = MaterialTheme.colorScheme.outline
    val searchBg = if (isDarkMode) TvDarkCard else TvBgLight

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            color = surfaceColor,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .border(1.dp, borderColor, RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Search Input Box
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(searchBg)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = TvGray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search markets, stocks, crypto...", fontSize = 14.sp, color = TvGray) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = onSurface,
                            unfocusedTextColor = onSurface
                        ),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { searchQuery = "" },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear",
                                tint = TvGray,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Filter pills: All, Stocks, Crypto, Forex, Futures
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val allSelected = selectedFilter == null
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(9999.dp))
                            .background(if (allSelected) TvBlue else Color.Transparent)
                            .clickable { selectedFilter = null }
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "All",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (allSelected) Color.White else TvGray
                        )
                    }

                    listOf(
                        MarketCategory.STOCKS,
                        MarketCategory.CRYPTO,
                        MarketCategory.FOREX,
                        MarketCategory.FUTURES
                    ).forEach { cat ->
                        val isSelected = selectedFilter == cat
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(9999.dp))
                                .background(if (isSelected) TvBlue else Color.Transparent)
                                .clickable { selectedFilter = cat }
                                .padding(horizontal = 12.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = cat.label,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSelected) Color.White else TvGray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = borderColor)
                Spacer(modifier = Modifier.height(8.dp))

                // Results List
                if (filteredAssets.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "No symbols found",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Try searching for AAPL, NVDA, BTC, or Gold",
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
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(filteredAssets) { asset ->
                            val isPositive = asset.change >= 0
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        onSelectAsset(asset)
                                        onDismiss()
                                    }
                                    .padding(horizontal = 8.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
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

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "$${String.format(Locale.US, "%,.2f", asset.price)}",
                                        fontSize = 13.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        color = onSurface
                                    )
                                    val sign = if (isPositive) "+" else ""
                                    Text(
                                        text = "$sign${String.format(Locale.US, "%.2f", asset.changePercent)}%",
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isPositive) TvGreen else TvRed
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
