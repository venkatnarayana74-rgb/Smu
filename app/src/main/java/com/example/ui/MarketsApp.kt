package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.MarketCategory
import com.example.model.MarketRegion
import com.example.ui.components.*
import com.example.viewmodel.MarketsViewModel

@Composable
fun MarketsApp(
    viewModel: MarketsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var isFullScreenerOpen by remember { mutableStateOf(false) }

    // Filter indices by region if a specific region is picked, otherwise show benchmark set
    val displayedIndices = remember(uiState.indices, uiState.selectedRegion, uiState.selectedCategory) {
        val regionIndices = when (uiState.selectedRegion) {
            MarketRegion.UNITED_STATES -> uiState.indices.filter { it.region == MarketRegion.UNITED_STATES }
            MarketRegion.EUROPE -> uiState.indices.filter { it.region == MarketRegion.EUROPE }
            MarketRegion.ASIA -> uiState.indices.filter { it.region == MarketRegion.ASIA }
            MarketRegion.AMERICAS -> uiState.indices.filter { it.region == MarketRegion.AMERICAS || it.region == MarketRegion.UNITED_STATES }
            MarketRegion.WORLD -> uiState.indices
        }
        if (regionIndices.isEmpty()) uiState.indices else regionIndices
    }

    // Filter table assets by Category and Screener Tab
    val displayedAssets = remember(uiState.assets, uiState.selectedCategory, uiState.selectedScreenerTab) {
        val categoryFiltered = when (uiState.selectedCategory) {
            MarketCategory.OVERVIEW -> uiState.assets
            MarketCategory.INDICES -> uiState.assets.filter { it.category == MarketCategory.INDICES || it.category == MarketCategory.STOCKS }
            MarketCategory.STOCKS -> uiState.assets.filter { it.category == MarketCategory.STOCKS }
            MarketCategory.CRYPTO -> uiState.assets.filter { it.category == MarketCategory.CRYPTO }
            MarketCategory.FOREX -> uiState.assets.filter { it.category == MarketCategory.FOREX }
            MarketCategory.FUTURES -> uiState.assets.filter { it.category == MarketCategory.FUTURES }
            MarketCategory.BONDS -> uiState.assets.filter { it.category == MarketCategory.BONDS }
            MarketCategory.WORLD_ECONOMY -> uiState.assets
        }

        categoryFiltered.filter { asset ->
            asset.tabs.contains(uiState.selectedScreenerTab) || uiState.selectedCategory != MarketCategory.OVERVIEW
        }.ifEmpty { categoryFiltered }
    }

    val watchlistAssets = remember(uiState.assets, uiState.watchlist) {
        uiState.assets.filter { uiState.watchlist.contains(it.symbol) }
    }

    Scaffold(
        topBar = {
            TradingViewHeader(
                isDarkMode = uiState.isDarkMode,
                onToggleDarkMode = { viewModel.toggleDarkMode() },
                onOpenSearch = { viewModel.openSearch() },
                onOpenWatchlist = { viewModel.openWatchlist() },
                watchlistCount = uiState.watchlist.size,
                onGetStartedClick = { viewModel.openGetStarted() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            // Horizontal Categories sub-navigation
            MarketCategoryNav(
                selectedCategory = uiState.selectedCategory,
                onSelectCategory = { viewModel.selectCategory(it) },
                isDarkMode = uiState.isDarkMode
            )

            // Hero section: "Markets, everywhere" + Region filters
            HeroSection(
                selectedRegion = uiState.selectedRegion,
                onSelectRegion = { viewModel.selectRegion(it) },
                onDropdownClick = { viewModel.openSearch() },
                isDarkMode = uiState.isDarkMode
            )

            // Indices Carousel / Grid
            IndicesSection(
                indices = displayedIndices,
                onSelectIndex = { viewModel.selectIndex(it) },
                isDarkMode = uiState.isDarkMode
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Financial Data Table / Screener
            MarketScreenerTable(
                assets = displayedAssets,
                selectedTab = uiState.selectedScreenerTab,
                onSelectTab = { viewModel.selectScreenerTab(it) },
                watchlistSymbols = uiState.watchlist,
                onToggleWatchlist = { viewModel.toggleWatchlist(it) },
                onSelectAsset = { viewModel.selectAsset(it) },
                onViewAllClick = { isFullScreenerOpen = true },
                isDarkMode = uiState.isDarkMode
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Footer
            TradingViewFooter()
        }

        // Selected Asset Detail BottomSheet
        uiState.selectedAsset?.let { asset ->
            AssetDetailSheet(
                asset = asset,
                isFavorite = uiState.watchlist.contains(asset.symbol),
                onToggleFavorite = { viewModel.toggleWatchlist(asset.symbol) },
                onDismiss = { viewModel.selectAsset(null) },
                onTradeClick = { isBuy ->
                    viewModel.openTradeDialog(asset, isBuy)
                },
                isDarkMode = uiState.isDarkMode
            )
        }

        // Selected Index Detail BottomSheet
        uiState.selectedIndex?.let { index ->
            IndexDetailDialog(
                index = index,
                allAssets = uiState.assets,
                onSelectAsset = { viewModel.selectAsset(it) },
                onDismiss = { viewModel.selectIndex(null) },
                isDarkMode = uiState.isDarkMode
            )
        }

        // Search Dialog
        if (uiState.isSearchOpen) {
            SearchDialog(
                assets = uiState.assets,
                onSelectAsset = { viewModel.selectAsset(it) },
                onDismiss = { viewModel.closeSearch() },
                isDarkMode = uiState.isDarkMode
            )
        }

        // Watchlist BottomSheet
        if (uiState.isWatchlistOpen) {
            WatchlistSheet(
                watchlistAssets = watchlistAssets,
                onSelectAsset = { viewModel.selectAsset(it) },
                onRemoveFromWatchlist = { viewModel.toggleWatchlist(it) },
                onDismiss = { viewModel.closeWatchlist() }
            )
        }

        // Full Screener BottomSheet
        if (isFullScreenerOpen) {
            FullScreenerSheet(
                assets = uiState.assets,
                watchlistSymbols = uiState.watchlist,
                onToggleWatchlist = { viewModel.toggleWatchlist(it) },
                onSelectAsset = { viewModel.selectAsset(it) },
                onDismiss = { isFullScreenerOpen = false },
                isDarkMode = uiState.isDarkMode
            )
        }

        // Trade Simulation Dialog
        uiState.tradeDialogState?.let { (asset, isBuy) ->
            TradeSimulationDialog(
                asset = asset,
                initialIsBuy = isBuy,
                onDismiss = { viewModel.closeTradeDialog() },
                isDarkMode = uiState.isDarkMode
            )
        }

        // Get Started Dialog
        if (uiState.isGetStartedOpen) {
            GetStartedDialog(
                onDismiss = { viewModel.closeGetStarted() },
                isDarkMode = uiState.isDarkMode
            )
        }
    }
}
