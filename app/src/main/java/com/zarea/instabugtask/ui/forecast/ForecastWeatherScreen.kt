package com.zarea.instabugtask.ui.forecast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zarea.instabugtask.di.DIContainer
import com.zarea.instabugtask.ui.common.components.ErrorView
import com.zarea.instabugtask.ui.common.components.ForecastContent
import com.zarea.instabugtask.ui.common.components.LoadingView
import com.zarea.instabugtask.ui.common.components.LocationDisabledView
import com.zarea.instabugtask.ui.common.components.PermissionRequiredView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastWeatherScreen(
    viewModel: ForecastViewModel = DIContainer.forecastViewModel,
    onNavigateToCurrentWeather: () -> Unit,
    onRequestLocationPermission: () -> Unit,
    onRequestEnableLocation: () -> Unit,
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Forecast Weather",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = { viewModel.loadWeather() }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when {
                    state.isLoading -> LoadingView()
                    state.error != null -> ErrorView(state.error!!, onRetry = viewModel::loadWeather)
                    state.locationPermissionRequired -> PermissionRequiredView(
                        onRequestPermission = {
                            viewModel.resetPermissionState()
                            onRequestLocationPermission()
                        }
                    )
                    state.locationServiceEnabledRequired -> LocationDisabledView(
                        onEnableLocation = {
                            viewModel.resetLocationServiceEnabledState()
                            onRequestEnableLocation()
                        }
                    )
                    state.forecast != null -> ForecastContent(state.forecast!!)
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = onNavigateToCurrentWeather,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Current Weather",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}