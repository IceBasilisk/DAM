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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import a47514.masterplanner.R

import a47514.masterplanner.Screen
import a47514.masterplanner.data.Roadmap
import a47514.masterplanner.data.RoadmapViewModel
import androidx.compose.foundation.lazy.items
import androidx.core.graphics.toColorInt

@Composable
fun MainMenuScreen(
    roadmapViewModel: RoadmapViewModel,
    onLogoutClick: () -> Unit = {},
    onNavigate: (Screen, String?) -> Unit = { _, _ -> }
) {
    val roadmaps by roadmapViewModel.roadmaps.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    val context = androidx.compose.ui.platform.LocalContext.current
    val isOnline by roadmapViewModel.isOnline.collectAsState()

    Scaffold(
        topBar = { MasterPlannerTopBar(onLogoutClick) },
        bottomBar = {
            MasterPlannerBottomBar(
                currentScreen = Screen.MainMenu,
                onNavigate = { screen -> onNavigate(screen, null) }
            )
        },
        containerColor = colorResource(R.color.fresh_cream)
    ) { innerPadding ->
        if (showDialog) {
            CreateRoadmapDialog(
                onDismiss = { showDialog = false },
                onConfirm = { title, iconName, colorHex ->
                    val newRoadmap = Roadmap(
                        title = title,
                        iconName = iconName,
                        colorHex = colorHex
                    )
                    roadmapViewModel.saveRoadmap(newRoadmap) { success, _ ->
                        if (success) {
                            showDialog = false
                        } else {
                            a47514.masterplanner.data.Utility.showToast(
                                context,
                                "Failed to save roadmap"
                            )
                        }
                    }
                }
            )
        }
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            OfflineBanner(isOnline = isOnline)

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(roadmaps) { roadmap ->
                    RoadmapCard(
                        title = roadmap.title,
                        iconName = roadmap.iconName,
                        colorHex = roadmap.colorHex,
                        onClick = { onNavigate(Screen.RoadMapEditor, roadmap.id) },
                        onDeleteRoadmap = { deleteTasks ->
                            roadmapViewModel.deleteRoadmapWithTasks(roadmap.id, deleteTasks) {}
                        }
                    )
                }

                item {
                    Button(
                        onClick = {
                            if (roadmaps.size >= 3) {
                                onNavigate(Screen.Freemium, null)
                            } else {
                                showDialog = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(vertical = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.gold)),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                        border = BorderStroke(width = 2.dp, color = colorResource(R.color.cigar))
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.AddLocationAlt,
                                contentDescription = null,
                                tint = colorResource(R.color.cigar),
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.create_roadmap_button),
                                color = colorResource(R.color.cigar),
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
}

@Composable
fun MasterPlannerTopBar(onLogoutClick: () -> Unit = {}) {
    val brown = colorResource(R.color.cigar)
    val cream = colorResource(R.color.fresh_cream)
    var showMenu by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
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
                modifier = Modifier
                    .background(cream)
                    .border(1.dp, brown, RoundedCornerShape(8.dp))
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
fun RoadmapCard(
    title: String,
    iconName: String = "",
    colorHex: String = "#A3BF95",
    onClick: () -> Unit = {},
    onDeleteRoadmap: (deleteTasks: Boolean) -> Unit = {}
) {
    val brown = colorResource(R.color.cigar)
    val cream = colorResource(R.color.fresh_cream)
    val cardBg = try {
        Color(colorHex.toColorInt())
    } catch (e: Exception) {
        colorResource(R.color.lauren)
    }

    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Confirmation dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = cream,
            title = {
                Text("Delete Roadmap", color = brown, fontWeight = FontWeight.ExtraBold)
            },
            text = {
                Text(
                    "Would you like to also delete all tasks inside \"$title\"?",
                    color = brown
                )
            },
            confirmButton = {
                Button(
                    onClick = { showDeleteDialog = false; onDeleteRoadmap(true) },
                    colors = ButtonDefaults.buttonColors(containerColor = brown)
                ) { Text("Delete All", color = cream, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { showDeleteDialog = false; onDeleteRoadmap(false) },
                        border = BorderStroke(2.dp, brown)
                    ) { Text("Keep Tasks", color = brown, fontWeight = FontWeight.Bold) }
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel", color = brown)
                    }
                }
            }
        )
    }

    Box(modifier = Modifier.clickable { onClick() }) {
        // Hard shadow
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = 6.dp, y = 6.dp)
                .background(brown, RoundedCornerShape(24.dp))
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = BorderStroke(width = 2.dp, color = brown)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.3f))
                        .border(1.dp, brown, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = a47514.masterplanner.data.Utility.iconFromName(iconName),
                        contentDescription = null, tint = brown,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                Text(
                    text = title,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold, color = brown
                )

                // Pencil menu
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            Icons.Default.Edit, contentDescription = "Options",
                            tint = brown, modifier = Modifier.size(20.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier
                            .background(cream)
                            .border(1.dp, brown, RoundedCornerShape(8.dp))
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Delete Roadmap",
                                    color = brown,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            onClick = { showMenu = false; showDeleteDialog = true },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = brown
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CreateRoadmapDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, iconName: String, colorHex: String) -> Unit
) {
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
                        onClick = {
                            if (title.isNotBlank()) {
                                val iconNames = listOf("Ship", "Flag", "Anchor", "Compass")
                                val colorHexValues = listOf("#FFD700", "#3DBEFF", "#FFD0BF")
                                onConfirm(
                                    title,
                                    iconNames.getOrElse(selectedMark) { "Flag" },
                                    colorHexValues.getOrElse(selectedColor) { "#FFFFD700" }
                                )
                            }
                        },
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
