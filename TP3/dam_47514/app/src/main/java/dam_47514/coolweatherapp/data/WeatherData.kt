package dam_47514.coolweatherapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherData(
    val latitude: String, // Changed to String in MainActivity usage? Wait, let me check MainActivity
    val longitude: String,
    val timezone: String,
    @SerialName("current_weather") val current_weather: CurrentWeather,
    val hourly: Hourly
)

@Serializable
data class CurrentWeather(
    val temperature: Float,
    val windspeed: Float,
    val winddirection: Int,
    val weathercode: Int,
    val time: String
)

@Serializable
data class Hourly(
    val time: List<String>,
    @SerialName("temperature_2m") val temperature_2m: List<Float>,
    @SerialName("weather_code") val weather_code: List<Int>,
    @SerialName("pressure_msl") val pressure_msl: List<Double>,
    @SerialName("windspeed_10m") val windspeed_10m: List<Float>
)

enum class WMO_WeatherCode(var code: Int, var image: String) {
    CLEAR_SKY(0, "clear_"),
    MAINLY_CLEAR(1, "mostly_clear_"),
    PARTLY_CLOUDY(2, "partly_cloudy_"),
    OVERCAST(3, "cloudy"),
    FOG(45, "fog"),
    DEPOSITING_RIME_FOG(48, "fog"),
    DRIZZLE_LIGHT(51, "drizzle"),
    DRIZZLE_MODERATE(53, "drizzle"),
    DRIZZLE_DENSE(55, "drizzle"),
    FREEZING_DRIZZLE_LIGHT(56, "freezing_drizzle"),
    FREEZING_DRIZZLE_DENSE(57, "freezing_drizzle"),
    RAIN_SLIGHT(61, "rain_light"),
    RAIN_MODERATE(63, "rain"),
    RAIN_HEAVY(65, "rain_heavy"),
    FREEZING_RAIN_LIGHT(66, "freezing_rain_light"),
    FREEZING_RAIN_HEAVY(67, "freezing_rain_heavy"),
    SNOW_FALL_SLIGHT(71, "snow_light"),
    SNOW_FALL_MODERATE(73, "snow"),
    SNOW_FALL_HEAVY(75, "snow_heavy"),
    SNOW_GRAINS(77, "snow"),
    RAIN_SHOWERS_SLIGHT(80, "rain_light"),
    RAIN_SHOWERS_MODERATE(81, "rain"),
    RAIN_SHOWERS_VIOLENT(82, "rain_heavy"),
    SNOW_SHOWERS_SLIGHT(85, "snow_light"),
    SNOW_SHOWERS_HEAVY(86, "snow_heavy"),
    THUNDERSTORM_SLIGHT_MODERATE(95, "tstorm"),
    THUNDERSTORM_HAIL_SLIGHT(96, "tstorm"),
    THUNDERSTORM_HAIL_HEAVY(99, "tstorm")
}

fun getWeatherCodeMap(): Map<Int, WMO_WeatherCode> {
    var weatherMap = HashMap<Int, WMO_WeatherCode>()
    WMO_WeatherCode.values().forEach {
        weatherMap.put(it.code, it)
    }
    return weatherMap
}
