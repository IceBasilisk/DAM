package dam_47514.coolweatherapp.ui

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dam_47514.coolweatherapp.data.WMO_WeatherCode
import dam_47514.coolweatherapp.data.getWeatherCodeMap
import dam_47514.coolweatherapp.viewmodel.WeatherViewModel
import dam_47514.coolweatherapp.ui.WeatherUIState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherUI(weatherViewModel: WeatherViewModel = viewModel()) {
    val weatherUIState by weatherViewModel.uiState.collectAsState()
    val latitude = weatherUIState.latitude
    val longitude = weatherUIState.longitude

    val weatherData = weatherUIState.weatherData
    val temperature = weatherData?.current_weather?.temperature ?: 0f
    val windSpeed = weatherData?.current_weather?.windspeed ?: 0f
    val windDirection = weatherData?.current_weather?.winddirection ?: 0
    val weathercode = weatherData?.current_weather?.weathercode ?: 0
    val seaLevelPressure = weatherData?.hourly?.pressure_msl?.getOrNull(12)?.toFloat() ?: 0f
    val time = weatherData?.current_weather?.time ?: ""

    val configuration = LocalConfiguration.current
    val day = true // Must change this in the future
    val mapt = getWeatherCodeMap()
    val wCode = mapt.get(weathercode)
    val wImage = when (wCode) {
        WMO_WeatherCode.CLEAR_SKY,
        WMO_WeatherCode.MAINLY_CLEAR,
        WMO_WeatherCode.PARTLY_CLOUDY -> if (day) wCode.image + "day"
        else wCode.image + "night"

        else -> wCode?.image
    }
    val context = LocalContext.current
    val wIcon = if (wImage != null) context.resources.getIdentifier(
        wImage,
        "drawable",
        context.packageName
    ) else 0

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Jetpack Cool Weather App") })
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                LandscapeWeatherUI(
                    wIcon,
                    latitude.toFloatOrNull() ?: 0f,
                    longitude.toFloatOrNull() ?: 0f,
                    temperature,
                    windSpeed,
                    windDirection,
                    weathercode,
                    seaLevelPressure,
                    time,
                    onLatitudeChange = { newValue ->
                        weatherViewModel.updateLatitude(newValue)
                    },
                    onLongitudeChange = { newValue ->
                        weatherViewModel.updateLongitude(newValue)
                    },
                    onUpdateButtonClick = {
                        weatherViewModel.fetchWeather()
                    }
                )
            } else {
                PortraitWeatherUI(
                    wIcon,
                    latitude.toFloatOrNull() ?: 0f,
                    longitude.toFloatOrNull() ?: 0f,
                    temperature,
                    windSpeed,
                    windDirection,
                    weathercode,
                    seaLevelPressure,
                    time,
                    onLatitudeChange = { newValue ->
                        weatherViewModel.updateLatitude(newValue)
                    },
                    onLongitudeChange = { newValue ->
                        weatherViewModel.updateLongitude(newValue)
                    },
                    onUpdateButtonClick = {
                        weatherViewModel.fetchWeather()
                    }
                )
            }
        }
    }
}

@Composable
fun PortraitWeatherUI(
    wIcon: Int,
    latitude: Float,
    longitude: Float,
    temperature: Float,
    windSpeed: Float,
    windDirection: Int,
    weathercode: Int,
    seaLevelPressure: Float,
    time: String,
    onLatitudeChange: (String) -> Unit,
    onLongitudeChange: (String) -> Unit,
    onUpdateButtonClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CoordinatesCard(
            latitude = latitude.toString(),
            longitude = longitude.toString(),
            onLatitudeChange = onLatitudeChange,
            onLongitudeChange = onLongitudeChange,
            onUpdateClick = onUpdateButtonClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Basic display of weather info since we're keeping the parameters separate
        if (wIcon != 0) {
            androidx.compose.foundation.Image(
                painter = androidx.compose.ui.res.painterResource(id = wIcon),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
        }
        Text(text = "$temperature ºC", style = MaterialTheme.typography.displayMedium)
        Text(text = "Time: $time")

        WeatherRow(label = "Wind Speed", value = "$windSpeed km/h")
        WeatherRow(label = "Wind Direction", value = "$windDirection º")
        WeatherRow(label = "Pressure", value = "$seaLevelPressure hPa")
    }
}

@Composable
fun LandscapeWeatherUI(
    wIcon: Int, latitude: Float, longitude: Float, temperature: Float, windSpeed: Float,
    windDirection: Int,
    weathercode: Int,
    seaLevelPressure: Float,
    time: String,
    onLatitudeChange: (String) -> Unit,
    onLongitudeChange: (String) -> Unit,
    onUpdateButtonClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            CoordinatesCard(
                latitude = latitude.toString(),
                longitude = longitude.toString(),
                onLatitudeChange = onLatitudeChange,
                onLongitudeChange = onLongitudeChange,
                onUpdateClick = onUpdateButtonClick
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (wIcon != 0) {
                androidx.compose.foundation.Image(
                    painter = androidx.compose.ui.res.painterResource(id = wIcon),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )
            }
            Text(text = "$temperature ºC", style = MaterialTheme.typography.headlineMedium)
            WeatherRow(label = "Wind", value = "$windSpeed km/h")
            WeatherRow(label = "Pressure", value = "$seaLevelPressure hPa")
        }
    }
}