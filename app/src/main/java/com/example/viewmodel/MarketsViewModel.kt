package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MarketDataRepository
import com.example.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

data class MarketsUiState(
    val indices: List<MarketIndex> = emptyList(),
    val assets: List<MarketAsset> = emptyList(),
    val selectedCategory: MarketCategory = MarketCategory.OVERVIEW,
    val selectedRegion: MarketRegion = MarketRegion.UNITED_STATES,
    val selectedScreenerTab: ScreenerTab = ScreenerTab.TRENDING,
    val watchlist: Set<String> = setOf("NVDA", "BTCUSD"),
    val isDarkMode: Boolean = false,
    val isSearchOpen: Boolean = false,
    val isWatchlistOpen: Boolean = false,
    val selectedAsset: MarketAsset? = null,
    val selectedIndex: MarketIndex? = null,
    val tradeDialogState: Pair<MarketAsset, Boolean>? = null,
    val isGetStartedOpen: Boolean = false,
    val isLiveUpdating: Boolean = true
)

class MarketsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        MarketsUiState(
            indices = MarketDataRepository.getIndices(),
            assets = MarketDataRepository.getAssets()
        )
    )
    val uiState: StateFlow<MarketsUiState> = _uiState.asStateFlow()

    init {
        startLiveSimulation()
    }

    private fun startLiveSimulation() {
        viewModelScope.launch {
            while (true) {
                delay(3000)
                if (_uiState.value.isLiveUpdating && _uiState.value.assets.isNotEmpty()) {
                    simulatePriceTick()
                }
            }
        }
    }

    private fun simulatePriceTick() {
        _uiState.update { state ->
            val randomAssetIndex = Random.nextInt(state.assets.size)
            val updatedAssets = state.assets.mapIndexed { index, asset ->
                if (index == randomAssetIndex) {
                    val deltaPercent = (Random.nextDouble(-0.15, 0.20))
                    val newPrice = (asset.price * (1.0 + deltaPercent / 100.0)).coerceAtLeast(0.01)
                    val newChange = newPrice - asset.prevClose
                    val newChangePercent = (newChange / asset.prevClose) * 100.0
                    val newHigh = maxOf(asset.high, newPrice)
                    val newLow = minOf(asset.low, newPrice)
                    asset.copy(
                        price = newPrice,
                        change = newChange,
                        changePercent = newChangePercent,
                        high = newHigh,
                        low = newLow
                    )
                } else {
                    asset
                }
            }

            // Also randomly update an index
            val randomIdx = Random.nextInt(state.indices.size)
            val updatedIndices = state.indices.mapIndexed { index, mIndex ->
                if (index == randomIdx) {
                    val deltaPercent = (Random.nextDouble(-0.08, 0.12))
                    val newPrice = mIndex.price * (1.0 + deltaPercent / 100.0)
                    val newChange = mIndex.change + (newPrice - mIndex.price)
                    val newChangePercent = (newChange / (newPrice - newChange)) * 100.0
                    mIndex.copy(
                        price = newPrice,
                        change = newChange,
                        changePercent = newChangePercent
                    )
                } else {
                    mIndex
                }
            }

            state.copy(
                assets = updatedAssets,
                indices = updatedIndices
            )
        }
    }

    fun selectCategory(category: MarketCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun selectRegion(region: MarketRegion) {
        _uiState.update { it.copy(selectedRegion = region) }
    }

    fun selectScreenerTab(tab: ScreenerTab) {
        _uiState.update { it.copy(selectedScreenerTab = tab) }
    }

    fun toggleWatchlist(symbol: String) {
        _uiState.update { state ->
            val next = state.watchlist.toMutableSet()
            if (next.contains(symbol)) {
                next.remove(symbol)
            } else {
                next.add(symbol)
            }
            state.copy(watchlist = next)
        }
    }

    fun toggleDarkMode() {
        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    fun openSearch() {
        _uiState.update { it.copy(isSearchOpen = true) }
    }

    fun closeSearch() {
        _uiState.update { it.copy(isSearchOpen = false) }
    }

    fun openWatchlist() {
        _uiState.update { it.copy(isWatchlistOpen = true) }
    }

    fun closeWatchlist() {
        _uiState.update { it.copy(isWatchlistOpen = false) }
    }

    fun selectAsset(asset: MarketAsset?) {
        _uiState.update { it.copy(selectedAsset = asset) }
    }

    fun selectIndex(index: MarketIndex?) {
        _uiState.update { it.copy(selectedIndex = index) }
    }

    fun openTradeDialog(asset: MarketAsset, isBuy: Boolean) {
        _uiState.update { it.copy(tradeDialogState = Pair(asset, isBuy)) }
    }

    fun closeTradeDialog() {
        _uiState.update { it.copy(tradeDialogState = null) }
    }

    fun openGetStarted() {
        _uiState.update { it.copy(isGetStartedOpen = true) }
    }

    fun closeGetStarted() {
        _uiState.update { it.copy(isGetStartedOpen = false) }
    }
}
