package com.zarea.instabugtask.di

import android.content.Context
import android.location.LocationManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zarea.instabugtask.data.location.LocationProvider
import com.zarea.instabugtask.data.location.LocationProviderImpl
import com.zarea.instabugtask.data.mapper.ForecastMapper
import com.zarea.instabugtask.data.mapper.LocationMapper
import com.zarea.instabugtask.data.mapper.WeatherMapper
import com.zarea.instabugtask.data.repository.LocationRepositoryImpl
import com.zarea.instabugtask.data.repository.WeatherRepositoryImpl
import com.zarea.instabugtask.data.source.local.SharedPreferencesDataSource
import com.zarea.instabugtask.data.source.remote.NetworkUtils
import com.zarea.instabugtask.data.source.remote.WeatherApi
import com.zarea.instabugtask.domain.repository.LocationRepository
import com.zarea.instabugtask.domain.repository.WeatherRepository
import com.zarea.instabugtask.domain.usecase.GetCurrentWeatherUseCase
import com.zarea.instabugtask.domain.usecase.GetForecastUseCase
import com.zarea.instabugtask.ui.permissionManager.PermissionManager
import com.zarea.instabugtask.ui.currentweather.CurrentWeatherViewModel
import com.zarea.instabugtask.ui.forecast.ForecastViewModel

object DIContainer {
    private lateinit var applicationContext: Context

    fun init(context: Context) {
        applicationContext = context.applicationContext
    }

    val permissionManager: PermissionManager by lazy { PermissionManager(applicationContext) }
    private val locationMapper by lazy { LocationMapper() }

    private val locationManager by lazy {
        applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    val locationProvider: LocationProvider by lazy { LocationProviderImpl(locationManager) }
    private val sharedPrefsDataSource by lazy { SharedPreferencesDataSource(applicationContext) }

    private val locationRepository: LocationRepository by lazy {
        LocationRepositoryImpl(
            locationMapper, sharedPrefsDataSource, locationProvider
        )
    }
    private val weatherApi: WeatherApi by lazy { WeatherApi() }
    private val networkUtils: NetworkUtils by lazy { NetworkUtils(applicationContext) }
    private val weatherMapper: WeatherMapper by lazy { WeatherMapper() }
    private val forecastMapper: ForecastMapper by lazy { ForecastMapper() }
    private val weatherRepository: WeatherRepository by lazy {
        WeatherRepositoryImpl(
            weatherApi = weatherApi,
            sharedPrefsDataSource = sharedPrefsDataSource,
            networkUtils = networkUtils,
            weatherMapper = weatherMapper,
            forecastMapper = forecastMapper
        )
    }

    val getCurrentWeatherUseCase: GetCurrentWeatherUseCase by lazy {
        GetCurrentWeatherUseCase(
            locationRepository = locationRepository,
            weatherRepository = weatherRepository
        )
    }
    val getForecastUseCase: GetForecastUseCase by lazy {
        GetForecastUseCase(
            locationRepository = locationRepository,
            weatherRepository = weatherRepository
        )
    }
    val currentWeatherViewModel: CurrentWeatherViewModel by lazy {
        CurrentWeatherViewModelFactory(getCurrentWeatherUseCase).create(CurrentWeatherViewModel::class.java)
    }
    val forecastViewModel: ForecastViewModel by lazy {
        ForecastWeatherViewModelFactory(getForecastUseCase).create(ForecastViewModel::class.java)
    }
}

class CurrentWeatherViewModelFactory(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrentWeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CurrentWeatherViewModel(getCurrentWeatherUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class ForecastWeatherViewModelFactory(
    private val getForecastUseCase: GetForecastUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForecastViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ForecastViewModel(getForecastUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}