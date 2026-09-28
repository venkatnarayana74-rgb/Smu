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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
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
import com.example.model.MarketCategory
import com.example.ui.theme.*
import java.util.Locale

enum class SortField {
    SYMBOL, PRICE, CHANGE_PERCENT, VOLUME
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenerSheet(
    assets: List<MarketAsset>,
    watchlistSymbols: Set<String>,
    onToggleWatchlist: (String) -> Unit,
    onSelectAsset: (MarketAsset) -> Unit,
    onDismiss: () -> Unit,
    isDarkMode: Boolean
) {
    var sortField by remember { mutableStateOf(SortField.CHANGE_PERCENT) }
    var sortAscending by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<MarketCategory?>(null) }

    val sortedAssets = remember(assets, sortField, sortAscending, selectedCategory) {
        val filtered = if (selectedCategory != null) {
            assets.filter { it.category == selectedCategory }
        } else {
            assets
        }

        when (sortField) {
            SortField.SYMBOL -> if (sortAscending) filtered.sortedBy { it.symbol } else filtered.sortedByDescending { it.symbol }
            SortField.PRICE -> if (sortAscending) filtered.sortedBy { it.price } else filtered.sortedByDescending { it.price }
            SortField.CHANGE_PERCENT -> if (sortAscending) filtered.sortedBy { it.changePercent } else filtered.sortedByDescending { it.changePercent }
            SortField.VOLUME -> if (sortAscending) filtered.sortedBy { it.volume } else filtered.sortedByDescending { it.volume }
        }
    }

    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurface = MaterialTheme.colorScheme.onSurface
    val borderColor = MaterialTheme.colorScheme.outline
    val tableHeadBg = if (isDarkMode) TvDarkCard else TvTableHead

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = surfaceColor,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Full Market Screener",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = onSurface
                    )
                    Text(
                        text = "${sortedAssets.size} assets listed",
                        fontSize = 12.sp,
                        color = TvGray
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = onSurface)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Category Filter Pills
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val allSelected = selectedCategory == null
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(9999.dp))
                        .background(if (allSelected) TvBlue else Color.Transparent)
                        .clickable { selectedCategory = null }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "All Categories",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (allSelected) Color.White else TvGray
                    )
                }

                listOf(MarketCategory.STOCKS, MarketCategory.CRYPTO, MarketCategory.FOREX, MarketCategory.FUTURES).forEach { cat ->
                    val isSelected = selectedCategory == cat
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(9999.dp))
                        .background(if (isSelected) TvBlue else Color.Transparent)
                        .clickable { selectedCategory = cat }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
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

            // Table with sortable column headers
            val tableScrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .horizontalScroll(tableScrollState)
            ) {
                // Header Row
                Row(
                    modifier = Modifier
                        .width(760.dp)
                        .background(tableHeadBg)
                        .border(1.dp, borderColor)
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SortableHeaderCell("SYMBOL", SortField.SYMBOL, sortField, sortAscending, Modifier.width(220.dp)) {
                        if (sortField == SortField.SYMBOL) sortAscending = !sortAscending else { sortField = SortField.SYMBOL; sortAscending = true }
                    }
                    SortableHeaderCell("PRICE (USD)", SortField.PRICE, sortField, sortAscending, Modifier.width(110.dp)) {
                        if (sortField == SortField.PRICE) sortAscending = !sortAscending else { sortField = SortField.PRICE; sortAscending = false }
                    }
                    SortableHeaderCell("CHANGE %", SortField.CHANGE_PERCENT, sortField, sortAscending, Modifier.width(100.dp)) {
                        if (sortField == SortField.CHANGE_PERCENT) sortAscending = !sortAscending else { sortField = SortField.CHANGE_PERCENT; sortAscending = false }
                    }
                    Text("HIGH", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TvGray, modifier = Modifier.width(85.dp))
                    Text("LOW", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TvGray, modifier = Modifier.width(85.dp))
                    SortableHeaderCell("VOLUME", SortField.VOLUME, sortField, sortAscending, Modifier.width(80.dp)) {
                        if (sortField == SortField.VOLUME) sortAscending = !sortAscending else { sortField = SortField.VOLUME; sortAscending = false }
                    }
                    Text("RATING", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TvGray, modifier = Modifier.width(80.dp))
                }

                // Table List
                LazyColumn(modifier = Modifier.width(760.dp)) {
                    items(sortedAssets) { asset ->
                        val isFavorite = watchlistSymbols.contains(asset.symbol)
                        val isPositive = asset.change >= 0

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(0.5.dp, borderColor)
                                .clickable {
                                    onSelectAsset(asset)
                                    onDismiss()
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Symbol
                            Row(
                                modifier = Modifier.width(220.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                IconButton(onClick = { onToggleWatchlist(asset.symbol) }, modifier = Modifier.size(24.dp)) {
                                    Icon(
                                        imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                        contentDescription = null,
                                        tint = if (isFavorite) Color(0xFFFFB300) else TvGray.copy(alpha = 0.5f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(asset.logoColor),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = asset.logoLetter, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                                Column {
                                    Text(text = asset.symbol, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = onSurface)
                                    Text(text = asset.name, fontSize = 11.sp, color = TvGray, maxLines = 1)
                                }
                            }

                            // Price
                            Text(
                                text = "$${String.format(Locale.US, "%,.2f", asset.price)}",
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = onSurface,
                                modifier = Modifier.width(110.dp)
                            )

                            // Change %
                            val sign = if (isPositive) "+" else ""
                            Text(
                                text = "$sign${String.format(Locale.US, "%.2f", asset.changePercent)}%",
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = if (isPositive) TvGreen else TvRed,
                                modifier = Modifier.width(100.dp)
                            )

                            // High
                            Text(text = "$${String.format(Locale.US, "%,.2f", asset.high)}", fontSize = 12.sp, fontFamily = FontFamily.Monospace, color = TvGray, modifier = Modifier.width(85.dp))

                            // Low
                            Text(text = "$${String.format(Locale.US, "%,.2f", asset.low)}", fontSize = 12.sp, fontFamily = FontFamily.Monospace, color = TvGray, modifier = Modifier.width(85.dp))

                            // Volume
                            Text(text = asset.volume, fontSize = 12.sp, fontFamily = FontFamily.Monospace, color = TvGray, modifier = Modifier.width(80.dp))

                            // Rating
                            Box(modifier = Modifier.width(80.dp)) {
                                TechnicalRatingBadge(rating = asset.technicalRating, isDarkMode = isDarkMode)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SortableHeaderCell(
    label: String,
    field: SortField,
    activeField: SortField,
    isAscending: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val isActive = field == activeField
    Row(
        modifier = modifier.clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isActive) TvBlue else TvGray
        )
        if (isActive) {
            Icon(
                imageVector = if (isAscending) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                contentDescription = null,
                tint = TvBlue,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}
