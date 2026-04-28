package dam_47514.coolweatherapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dam_47514.coolweatherapp.data.WeatherData

@Composable
fun WeatherCard(weatherData: WeatherData, iconResId: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (iconResId != 0) {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(100.dp)
                )
            }
            Text(
                text = "${weatherData.current_weather.temperature}ºC",
                style = MaterialTheme.typography.displayMedium
            )
            Text(text = weatherData.timezone, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            WeatherRow(label = "Wind Speed", value = "${weatherData.current_weather.windspeed} km/h")
            WeatherRow(label = "Wind Direction", value = "${weatherData.current_weather.winddirection}º")
            
            val pressure = weatherData.hourly.pressure_msl.getOrNull(12)
            if (pressure != null) {
                WeatherRow(label = "Pressure", value = "$pressure hPa")
            }
        }
    }
}
