package com.example.model

import androidx.compose.ui.graphics.Color

enum class MarketRegion(val label: String) {
    UNITED_STATES("United States"),
    WORLD("World"),
    AMERICAS("Americas"),
    EUROPE("Europe"),
    ASIA("Asia")
}

enum class MarketCategory(val label: String) {
    OVERVIEW("Overview"),
    INDICES("Indices"),
    STOCKS("Stocks"),
    CRYPTO("Crypto"),
    FOREX("Forex"),
    FUTURES("Futures"),
    BONDS("Bonds"),
    WORLD_ECONOMY("World Economy")
}

enum class ScreenerTab(val label: String) {
    TRENDING("Trending"),
    MOST_ACTIVE("Most Active"),
    GAINERS("Gainers"),
    LOSERS("Losers"),
    CRYPTOCURRENCIES("Cryptocurrencies")
}

enum class TechnicalRating(val label: String) {
    STRONG_BUY("Strong Buy"),
    BUY("Buy"),
    NEUTRAL("Neutral"),
    SELL("Sell"),
    STRONG_SELL("Strong Sell")
}

enum class ChartTimeframe(val label: String) {
    T_1D("1D"),
    T_5D("5D"),
    T_1M("1M"),
    T_6M("6M"),
    T_1Y("1Y"),
    T_ALL("ALL")
}

data class CandleStick(
    val timestamp: String,
    val open: Float,
    val high: Float,
    val low: Float,
    val close: Float
)

data class MarketIndex(
    val symbol: String,
    val name: String,
    val badgeText: String,
    val badgeColor: Color,
    val price: Double,
    val change: Double,
    val changePercent: Double,
    val sparkline: List<Float>,
    val region: MarketRegion
)

data class MarketAsset(
    val symbol: String,
    val name: String,
    val logoLetter: String,
    val logoColor: Color,
    val price: Double,
    val change: Double,
    val changePercent: Double,
    val high: Double,
    val low: Double,
    val volume: String,
    val technicalRating: TechnicalRating,
    val category: MarketCategory,
    val tabs: List<ScreenerTab>,
    val marketCap: String,
    val peRatio: Double?,
    val openPrice: Double,
    val prevClose: Double,
    val week52High: Double,
    val week52Low: Double,
    val sparkline: List<Float>,
    val candlesticks: List<CandleStick>,
    val description: String,
    val oscillatorRating: String = "Buy",
    val movingAverageRating: String = "Strong Buy"
)
