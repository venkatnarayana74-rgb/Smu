package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.ui.MarketsApp
import com.example.ui.theme.TradingViewTheme
import com.example.viewmodel.MarketsViewModel

class MainActivity : ComponentActivity() {
    private val marketsViewModel: MarketsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by marketsViewModel.uiState.collectAsState()
            TradingViewTheme(darkTheme = uiState.isDarkMode) {
                MarketsApp(viewModel = marketsViewModel)
            }
        }
    }
}
