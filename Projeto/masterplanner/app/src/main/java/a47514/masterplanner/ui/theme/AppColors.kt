package a47514.masterplanner.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

data class AppColors(
    val brown: Color,
    val cream: Color,
    val gold: Color,
    val lighterBrown: Color,
    val cheesecake: Color,
    val cigar: Color,
    val highlighterBlue: Color,
    val cocoaBrown: Color,
    val paradiseGreen: Color,
    val paleApricot: Color,
    val seaBlue: Color,
    val lauren: Color,
    val warmDarkBrown: Color,
    val white: Color = Color.White,
    val black: Color = Color.Black
)

val LocalAppColors = compositionLocalOf<AppColors> {
    error("No AppColors provided")
}