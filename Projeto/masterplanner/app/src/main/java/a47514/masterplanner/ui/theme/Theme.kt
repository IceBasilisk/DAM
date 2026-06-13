package a47514.masterplanner.ui.theme

import a47514.masterplanner.R
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun MasterPlannerTheme(content: @Composable () -> Unit) {
    val appColors = AppColors(
        brown = colorResource(R.color.cigar),
        cream = colorResource(R.color.fresh_cream),
        gold = colorResource(R.color.gold),
        lighterBrown = colorResource(R.color.old_rose),
        cheesecake = colorResource(R.color.cheesecake),
        cigar = colorResource(R.color.cigar),
        highlighterBlue = colorResource(R.color.highlighter_blue),
        cocoaBrown = colorResource(R.color.cocoa_brown),
        paradiseGreen = colorResource(R.color.paradise_green),
        paleApricot = colorResource(R.color.pale_apricot),
        seaBlue = colorResource(R.color.sea_blue),
        lauren = colorResource(R.color.lauren),
        warmDarkBrown = colorResource(R.color.warm_dark_brown)
    )

    CompositionLocalProvider(LocalAppColors provides appColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}