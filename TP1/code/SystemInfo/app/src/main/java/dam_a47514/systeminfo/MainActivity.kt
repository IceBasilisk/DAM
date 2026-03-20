package dam_a47514.systeminfo

import android.os.Bundle
import android.os.Build
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    val base: String? = Build.VERSION.BASE_OS.ifEmpty { "Not specified" }
    val info =
        "Manufacturer: " + Build.MANUFACTURER +
                "\nModel: " + Build.MODEL +
                "\nBrand: " + Build.BRAND +
                "\nType: " + Build.TYPE +
                "\nUser: " + Build.USER +
                "\nBase: " + base +
                "\nIncremental: " + Build.VERSION.INCREMENTAL +
                "\nSDK: " + Build.VERSION.SDK_INT +
                "\nVersion Code: " + Build.VERSION.RELEASE +
                "\nDisplay: " + Build.DISPLAY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.textDeviceInfo)
        textView.text = info
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}