package dam_47514.weathercompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam_47514.weathercompose.data.WeatherApiClient
import dam_47514.weathercompose.ui.WeatherUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(WeatherUIState())
    val uiState: StateFlow<WeatherUIState> = _uiState.asStateFlow()

    fun updateLatitude(lat: String) { _uiState.update { it.copy(latitude = lat) } }
    fun updateLongitude(lon: String) { _uiState.update { it.copy(longitude = lon) } }

    fun fetchWeather() {
        viewModelScope.launch {
            val data = WeatherApiClient.getWeather(_uiState.value.latitude, _uiState.value.longitude)

            data?.let { response ->
                val currentPressure = response.hourly.pressure_msl.firstOrNull()?.toFloat() ?: 0f

                val current = response.current_weather
                _uiState.update { it.copy(
                    seaLevelPressure = currentPressure,
                    winddirection = current.winddirection,
                    windspeed = current.windspeed,
                    temperature = current.temperature,
                    time = current.time,
                    timezone = data.timezone,
                    weathercode = current.weathercode
                ) }
            }
        }
    }
}