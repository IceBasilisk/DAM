package dam_47514.coolweatherapp.ui

import dam_47514.coolweatherapp.data.WeatherData

data class WeatherUIState(
    val latitude: String = "",
    val longitude: String = "",
    val weatherData: WeatherData? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)