package a47514.masterplanner.ui

import a47514.masterplanner.R
import a47514.masterplanner.Screen
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp

@Composable
fun MasterPlannerBottomBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit
) {
    val brown = colorResource(R.color.cigar)
    val cream = colorResource(R.color.fresh_cream)
    val gold = colorResource(R.color.gold)
    val lighterBrown = colorResource(R.color.old_rose)

    NavigationBar(
        containerColor = cream,
        tonalElevation = 8.dp,
        modifier = Modifier.clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
    ) {
        NavigationBarItem(
            selected = currentScreen == Screen.MainMenu,
            onClick = { onNavigate(Screen.MainMenu) },
            icon = { Icon(Icons.Default.Map, contentDescription = null) },
            label = { Text("ROADMAPS") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = brown,
                selectedTextColor = brown,
                indicatorColor = gold,
                unselectedIconColor = lighterBrown,
                unselectedTextColor = lighterBrown
            )
        )
        NavigationBarItem(
            selected = currentScreen == Screen.TaskLibrary,
            onClick = { onNavigate(Screen.TaskLibrary) },
            icon = { Icon(Icons.AutoMirrored.Filled.LibraryBooks, contentDescription = null) },
            label = { Text("LIBRARY") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = brown,
                selectedTextColor = brown,
                indicatorColor = gold,
                unselectedIconColor = lighterBrown,
                unselectedTextColor = lighterBrown
            )
        )
    }
}
