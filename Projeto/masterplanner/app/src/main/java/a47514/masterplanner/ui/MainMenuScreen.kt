package a47514.masterplanner.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainMenuScreen() {
    val cream = colorResource(R.color.fresh_cream)
    val brown = colorResource(R.color.cigar)
    val gold = colorResource(R.color.gold)
    val blue = colorResource(R.color.highlighter_blue)
    val pink = colorResource(R.color.pale_apricot)
    val yellow = colorResource(R.color.lighting_yellow)

    Scaffold(
        topBar = { MasterPlannerTopBar() },
        bottomBar = { MasterPlannerBottomBar() },
        containerColor = cream
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            item { FeaturedCard() }

            item {
                RoadmapCard(
                    category = "MAIN QUEST",
                    categoryColor = blue,
                    title = "Grand Mastery of Vue.js",
                    progress = "12 / 24 OBJECTIVES",
                    icon = Icons.Default.Explore
                )
            }

            item {
                RoadmapCard(
                    category = "SIDE VOYAGE",
                    categoryColor = pink,
                    title = "The Python Depths",
                    progress = "3 / 40 OBJECTIVES",
                    icon = Icons.Default.Sailing
                )
            }

            item {
                RoadmapCard(
                    category = "TREASURE HUNT",
                    categoryColor = yellow,
                    title = "Design System Lore",
                    progress = "9 / 10 OBJECTIVES",
                    icon = Icons.Default.Security
                )
            }

            item {
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = gold),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.AddLocationAlt,
                            contentDescription = null,
                            tint = brown,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "MAKE NEW ROADMAP",
                            color = brown,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun MasterPlannerTopBar() {
    val brown = colorResource(R.color.cigar)
    val cream = colorResource(R.color.fresh_cream)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .statusBarsPadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Pirate Avatar Placeholder
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(brown)
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                tint = cream,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Text(
            "MASTER PLANNER",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = brown,
            letterSpacing = 1.sp
        )

        Icon(
            Icons.Default.Search,
            contentDescription = "Search",
            tint = brown,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
fun FeaturedCard() {
    val cigar = colorResource(R.color.cigar)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF8B4513), Color(0xFF432818))
                    )
                )
        ) {
            // Background Icon as watermark
            Icon(
                Icons.Default.Public,
                contentDescription = null,
                modifier = Modifier
                    .size(240.dp)
                    .align(Alignment.Center)
                    .offset(x = 60.dp, y = 20.dp),
                tint = Color.White.copy(alpha = 0.05f)
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp)
            ) {
                Text(
                    "Your Legend Awaits",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "4 Active Roadmaps",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun RoadmapCard(
    category: String,
    categoryColor: Color,
    title: String,
    progress: String,
    icon: ImageVector
) {
    val brown = colorResource(R.color.cigar)
    val gold = colorResource(R.color.gold)
    val cardBg = colorResource(R.color.cheesecake)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Faded background icon
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier
                    .size(140.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 30.dp, y = (-20).dp),
                tint = brown.copy(alpha = 0.05f)
            )

            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    color = categoryColor,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = category,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = brown
                    )
                }

                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = brown
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Token,
                        contentDescription = null,
                        tint = gold,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = progress,
                        style = MaterialTheme.typography.bodyMedium,
                        color = brown.copy(alpha = 0.7f),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Chevron arrow circle
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(36.dp),
                color = brown.copy(alpha = 0.1f),
                shape = RoundedCornerShape(18.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = brown
                    )
                }
            }
        }
    }
}

@Composable
fun MasterPlannerBottomBar() {
    val brown = colorResource(R.color.cigar)
    val cream = colorResource(R.color.fresh_cream)
    val gold = colorResource(R.color.gold)

    NavigationBar(
        containerColor = cream,
        tonalElevation = 8.dp,
        modifier = Modifier.clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Icon(Icons.Default.Map, contentDescription = null) },
            label = { Text("ROADMAPS") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = brown,
                selectedTextColor = brown,
                indicatorColor = gold
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.LibraryBooks, contentDescription = null) },
            label = { Text("LIBRARY") },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = brown.copy(alpha = 0.6f),
                unselectedTextColor = brown.copy(alpha = 0.6f)
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
            label = { Text("SETTINGS") },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = brown.copy(alpha = 0.6f),
                unselectedTextColor = brown.copy(alpha = 0.6f)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainMenuScreenPreview() {
    MainMenuScreen()
}
