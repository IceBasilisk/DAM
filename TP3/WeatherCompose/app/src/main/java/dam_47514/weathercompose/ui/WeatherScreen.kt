package dam_47514.weathercompose.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dam_47514.weathercompose.viewmodel.WeatherViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weathercompose.R
import dam_47514.weathercompose.data.WMO_WeatherCode
import dam_47514.weathercompose.data.getWeatherCodeMap

@Preview
@Composable
fun WeatherUI(weatherViewModel: WeatherViewModel = viewModel()) {
    val weatherUIState by weatherViewModel.uiState.collectAsState()
    val latitude = weatherUIState.latitude
    val longitude = weatherUIState.longitude
    val temperature = weatherUIState.temperature
    val windSpeed = weatherUIState.windspeed
    val windDirection = weatherUIState.winddirection
    val weathercode = weatherUIState.weathercode
    val seaLevelPressure = weatherUIState.seaLevelPressure
    val time = weatherUIState.time
    val configuration = LocalConfiguration.current
    val day = true // Must change this in the future
    val mapt = getWeatherCodeMap()
    val wCode = mapt.get(weathercode)
    val wImage = when (wCode) {
        WMO_WeatherCode.CLEAR_SKY,
        WMO_WeatherCode.MAINLY_CLEAR,
        WMO_WeatherCode.PARTLY_CLOUDY -> if (day) wCode?.image + "day"
        else wCode?.image + "night"

        else -> wCode?.image
    }
    val context = LocalContext.current
    val wIcon = context.resources.getIdentifier(
        wImage, "drawable",
        context.packageName
    )
    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        LandscapeWeatherUI(
            wIcon,
            latitude,
            longitude,
            temperature,
            windSpeed,
            windDirection,
            weathercode,
            seaLevelPressure,
            time,
            onLatitudeChange = { newValue ->
                newValue.toFloatOrNull()?.let {
                    weatherViewModel.updateLatitude(it)
                }
            },
            onLongitudeChange = { newValue ->
                newValue.toFloatOrNull()?.let {
                    weatherViewModel.updateLongitude(it)
                }
            },
            onUpdateButtonClick = {
                weatherViewModel.fetchWeather()
            }
        )
    } else {
        PortraitWeatherUI(
            wIcon,
            latitude,
            longitude,
            temperature,
            windSpeed,
            windDirection,
            weathercode,
            seaLevelPressure,
            time,
            onLatitudeChange = { newValue ->
                newValue.toFloatOrNull()?.let {
                    weatherViewModel.updateLatitude(it)
                }
            },
            onLongitudeChange = { newValue ->
                newValue.toFloatOrNull()?.let {
                    weatherViewModel.updateLongitude(it)
                }
            },
            onUpdateButtonClick = {
                weatherViewModel.fetchWeather()
            }
        )
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
    Column() {
        Card() {
            Column() {
                Text(
                    text = stringResource(R.string.coordinates_label),
                    style = typography.labelLarge,
                )
                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Latitude") }
                )

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Longitude") }
                )

            }
        }

        Card() {
            Column() {
                Row() {
                    Text(stringResource(R.string.sea_level_label), style = typography.labelSmall, textAlign = TextAlign.Left)
                    Text(stringResource(R.string.sea_level_label), style = typography.labelSmall, textAlign = TextAlign.Right)
                }
                Row() {
                    Text(stringResource(R.string.wind_direction_label), style = typography.labelSmall, textAlign = TextAlign.Left)
                    Text(stringResource(R.string.wind_direction_label), style = typography.labelSmall, textAlign = TextAlign.Right)
                }
                Row() {
                    Text(stringResource(R.string.wind_speed_label), style = typography.labelSmall, textAlign = TextAlign.Left)
                    Text(stringResource(R.string.wind_speed_label), style = typography.labelSmall, textAlign = TextAlign.Right)
                }
                Row() {
                    Text(stringResource(R.string.temperature_label), style = typography.labelSmall, textAlign = TextAlign.Left)
                    Text(stringResource(R.string.temperature_label), style = typography.labelSmall, textAlign = TextAlign.Right)
                }
                Row() {
                    Text(stringResource(R.string.time_label), style = typography.labelSmall, textAlign = TextAlign.Left)
                    Text(stringResource(R.string.time_label), style = typography.labelSmall, textAlign = TextAlign.Right)
                }
                Row() {
                }
            }
            Column() {

            }
        }
    }
}

@Composable
fun LandscapeWeatherUI(
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
// ToDo
}
