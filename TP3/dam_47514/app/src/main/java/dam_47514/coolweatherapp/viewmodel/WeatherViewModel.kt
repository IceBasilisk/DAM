package dam_47514.coolweatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam_47514.coolweatherapp.data.WeatherApiClient
import dam_47514.coolweatherapp.ui.WeatherUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUIState())
    val uiState: StateFlow<WeatherUIState> = _uiState.asStateFlow()

    fun updateLatitude(lat: String) {
        _uiState.update { it.copy(latitude = lat) }
    }

    fun updateLongitude(lon: String) {
        _uiState.update { it.copy(longitude = lon) }
    }

    fun fetchWeather() {
        val lat = _uiState.value.latitude.toFloatOrNull()
        val lon = _uiState.value.longitude.toFloatOrNull()

        if (lat == null || lon == null) {
            _uiState.update { it.copy(errorMessage = "Invalid coordinates") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val result = WeatherApiClient.getWeather(lat, lon)
                if (result != null) {
                    _uiState.update { it.copy(weatherData = result, isLoading = false) }
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Failed to fetch weather data") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Unknown error") }
            }
        }
    }
}
