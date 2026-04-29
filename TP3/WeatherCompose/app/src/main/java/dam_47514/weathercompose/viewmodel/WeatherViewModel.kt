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

    fun updateLatitude(lat: Float) { _uiState.update { it.copy(latitude = lat) } }
    fun updateLongitude(lon: Float) { _uiState.update { it.copy(longitude = lon) } }

    fun fetchWeather() {
        viewModelScope.launch {
            val data = WeatherApiClient.getWeather(_uiState.value.latitude, _uiState.value.longitude)
            data?.current_weather?.let { current ->
                _uiState.update { it.copy(
                    temperature = current.temperature,
                    windspeed = current.windspeed,
                    time = current.time
                ) }
            }
        }
    }
}