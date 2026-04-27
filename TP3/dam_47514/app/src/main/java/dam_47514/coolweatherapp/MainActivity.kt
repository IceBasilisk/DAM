package dam_47514.coolweatherapp

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import dam_47514.coolweatherapp.data.WMO_WeatherCode
import dam_47514.coolweatherapp.data.WeatherData
import dam_47514.coolweatherapp.data.getWeatherCodeMap
import java.io.InputStreamReader
import java.net.URL

class MainActivity : AppCompatActivity() {

    private var day: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        day =
            (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) != Configuration.UI_MODE_NIGHT_YES

        when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                if (day) {
                    setTheme(R.style.Theme_Day)
                } else {
                    setTheme(R.style.Theme_Night)
                }
            }

            Configuration.ORIENTATION_LANDSCAPE -> {
                if (day) {
                    setTheme(R.style.Theme_Day_Land)
                } else {
                    setTheme(R.style.Theme_Night_Land)
                }
            }
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val updateButton = findViewById<Button>(R.id.buttonUpdate)
        updateButton.setOnClickListener() {
            val lat = findViewById<EditText>(R.id.editTextLatitude).text.toString().toFloat()
            val long = findViewById<EditText>(R.id.editTextLongitude).text.toString().toFloat()
            fetchWeatherData(lat, long).start()
        }
    }

    private fun WeatherAPI_Call(lat: Float, long: Float): WeatherData {
        val reqString = buildString {
            append("https://api.open-meteo.com/v1/forecast?")
            append("latitude=${lat}&longitude=${long}&")
            append("current_weather=true&")
            append("hourly=temperature_2m,weathercode,pressure_msl,windspeed_10m&")
            append("timezone=auto")
        }
        val url = URL(reqString);
        url.openStream().use {
            val request = Gson().fromJson(InputStreamReader(it, "UTF-8"), WeatherData::class.java)
            return request
        }
    }

    private fun fetchWeatherData(lat: Float, long: Float): Thread {
        return Thread {
            val weather = WeatherAPI_Call(lat, long)
            updateUI(weather)
        }
    }

    private fun updateUI(request: WeatherData) {
        runOnUiThread {
            val latitude: TextView = findViewById(R.id.editTextLatitude)
            val longitude: TextView = findViewById(R.id.editTextLongitude)
            val weatherImage: ImageView = findViewById(R.id.imageWeather)
            val pressure: TextView = findViewById(R.id.textViewSeaLevelValue)
            val direction: TextView = findViewById(R.id.textViewWindDirectionValue)
            val speed: TextView = findViewById(R.id.textViewWindSpeedValue)
            val temperature: TextView = findViewById(R.id.textViewTemperatureValue)
            val time: TextView = findViewById(R.id.textViewTimeValue)
            val timezone: TextView = findViewById(R.id.textViewTimezone)
// TODO ...
            latitude.text = request.latitude
            longitude.text = request.longitude
            pressure.text = request.hourly.pressure_msl.get(12).toString() + " hPa"
            direction.text = request.current_weather.winddirection.toString()
            speed.text = request.current_weather.windspeed.toString() + " km/h"
            temperature.text = request.hourly.temperature_2m.get(12).toString() + " ºC"
            time.text = request.current_weather.time
            timezone.text = request.timezone
// TODO ...
            val mapt = getWeatherCodeMap();
            val wCode = mapt.get(request.current_weather.weathercode)
            val wImage = when (wCode) {
                WMO_WeatherCode.CLEAR_SKY,
                WMO_WeatherCode.MAINLY_CLEAR,
                WMO_WeatherCode.PARTLY_CLOUDY -> if (day) wCode?.image + "day" else
                    wCode?.image + "night"

                else -> wCode?.image
            }
            val res = getResources()
            weatherImage.setImageResource(R.drawable.fog)
            val resID = res.getIdentifier(wImage, "drawable", packageName);
            val drawable = this.getDrawable(resID);
            weatherImage.setImageDrawable(drawable);
// TODO ...
        }
    }


}