package a47514.masterplanner.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import a47514.masterplanner.R

import a47514.masterplanner.Screen

@Composable
fun MainMenuScreen(
    onLogoutClick: () -> Unit = {},
    onNavigate: (Screen) -> Unit = {}
) {
    var showDialog by remember { mutableStateOf(false) }
    val cream = colorResource(R.color.fresh_cream)
    val brown = colorResource(R.color.cigar)
    val gold = colorResource(R.color.gold)

    Scaffold(
        topBar = { MasterPlannerTopBar(onLogoutClick) },
        bottomBar = { 
            MasterPlannerBottomBar(
                currentScreen = Screen.MainMenu,
                onNavigate = onNavigate
            )
        },
        containerColor = cream
    ) { innerPadding ->
        if (showDialog) {
            CreateRoadmapDialog(onDismiss = { showDialog = false })
        }
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
                    title = stringResource(R.string.roadmap_title_test1),
                    onClick = { onNavigate(Screen.RoadMapEditor) }
                )
            }

            item {
                Button(
                    onClick = { showDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = gold),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    border = BorderStroke(width = 2.dp, color = brown)
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
                            text = stringResource(R.string.create_roadmap_button),
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
fun MasterPlannerTopBar(onLogoutClick: () -> Unit = {}) {
    val brown = colorResource(R.color.cigar)
    val cream = colorResource(R.color.fresh_cream)
    var showMenu by remember { mutableStateOf(false) }

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
                .clickable { showMenu = true }
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = "Account",
                tint = cream,
                modifier = Modifier.align(Alignment.Center)
            )

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                modifier = Modifier.background(cream).border(1.dp, brown, RoundedCornerShape(8.dp))
            ) {
                DropdownMenuItem(
                    text = { 
                        Text(
                            "Logout", 
                            color = brown, 
                            fontWeight = FontWeight.Bold 
                        ) 
                    },
                    onClick = {
                        showMenu = false
                        onLogoutClick()
                    },
                    leadingIcon = { 
                        Icon(
                            Icons.Default.Logout, 
                            contentDescription = null, 
                            tint = brown 
                        ) 
                    }
                )
            }
        }

        Text(
            text = stringResource(R.string.app_name),
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
                    "1 Active Roadmaps",
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
    title: String,
    onClick: () -> Unit = {}
) {
    val brown = colorResource(R.color.cigar)
    val cardBg = colorResource(R.color.lauren)
    Box(modifier = Modifier.clickable { onClick() }) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = 6.dp, y = 6.dp) // Shift down and right
                .background(
                    brown, RoundedCornerShape(24.dp)
                )
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = BorderStroke(width = 2.dp, color = brown)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = brown
                    )
                }
            }
        }
    }
}

// MainMenuBottomBar removed in favor of MasterPlannerBottomBar in CommonUI.kt

@Preview(showBackground = true)
@Composable
fun MainMenuScreenPreview() {
    MainMenuScreen()
}

@Composable
fun CreateRoadmapDialog(onDismiss: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var selectedMark by remember { mutableStateOf(0) }
    var selectedColor by remember { mutableStateOf(0) }

    val marks = listOf(
        Icons.Default.DirectionsBoat,
        Icons.Default.Map,
        Icons.Default.Anchor,
        Icons.Default.Explore
    )
    val colorOptions = listOf(
        colorResource(R.color.gold),
        colorResource(R.color.highlighter_blue),
        colorResource(R.color.pale_apricot)
    )

    val cigar = colorResource(R.color.cigar)
    val cream = colorResource(R.color.fresh_cream)
    val gold = colorResource(R.color.gold)
    val cheesecake = colorResource(R.color.cheesecake)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(28.dp),
            color = cream
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(cigar)
                        .padding(vertical = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "CHART NEW COURSE",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        letterSpacing = 1.sp
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth()
                ) {
                    // Title section
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = cigar
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "ROADMAP TITLE",
                            color = cigar,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        placeholder = { Text("e.g. Find the Lost Lagoon") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = cigar,
                            unfocusedBorderColor = cigar.copy(alpha = 0.5f)
                        )
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // Mark & Color section
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Flag,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = cigar
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "SELECT MARK & COLOR",
                            color = cigar,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Marks
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        marks.forEachIndexed { index, icon ->
                            val isSelected = selectedMark == index
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isSelected) gold else cheesecake)
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) cigar else Color.LightGray,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .clickable { selectedMark = index },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = cigar,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Colors
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        colorOptions.forEachIndexed { index, color ->
                            val isSelected = selectedColor == index
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 12.dp)
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(
                                        width = if (isSelected) 3.dp else 0.dp,
                                        color = if (isSelected) cigar else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable { selectedColor = index }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Cancel Button
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .border(2.dp, cigar, RoundedCornerShape(16.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = cheesecake),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "CANCEL",
                            color = cigar,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Set Sail Button
                    Button(
                        onClick = { /* Handle Create */ onDismiss() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .border(2.dp, cigar, RoundedCornerShape(16.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = gold),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "SET SAIL",
                                color = cigar,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                Icons.Default.DirectionsBoat,
                                contentDescription = null,
                                tint = cigar,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
