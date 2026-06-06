package a47514.masterplanner.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import a47514.masterplanner.R

import a47514.masterplanner.Screen

@Composable
fun RoadMapEditorScreen(
    onCreateTask: () -> Unit = {},
    onNavigate: (Screen) -> Unit = {}
) {
    val cream = colorResource(R.color.fresh_cream)
    val brown = colorResource(R.color.cigar)
    val gold = colorResource(R.color.gold)
    val apricot = colorResource(R.color.pale_apricot)
    val lighterBrown = colorResource(R.color.old_rose)

    var showBootyBag by remember { mutableStateOf(false) }
    var showDurationDialog by remember { mutableStateOf(false) }
    var selectedTaskIndexForDuration by remember { mutableStateOf(-1) }

    val initialItems = listOf(
        RoadmapItem.Task(stringResource(R.string.task_title_test1)),
        RoadmapItem.Task(stringResource(R.string.task_title_test2)),
        RoadmapItem.Task(stringResource(R.string.task_title_test3)),
        RoadmapItem.Duration(stringResource(R.string.duration_test1)),
        RoadmapItem.Task(stringResource(R.string.task_title_test4)),
        RoadmapItem.Task(stringResource(R.string.task_title_test5)),
        RoadmapItem.Task(stringResource(R.string.task_title_test6))
    )

    val currentItems = remember { mutableStateListOf<RoadmapItem>().apply { addAll(initialItems) } }

    Scaffold(
        topBar = { MasterPlannerTopBar() },
        bottomBar = { 
            MasterPlannerBottomBar(
                currentScreen = Screen.RoadMapEditor,
                onNavigate = onNavigate
            )
        },
        containerColor = cream,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBootyBag = true },
                containerColor = gold,
                contentColor = brown,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .size(64.dp)
                    .border(2.dp, brown, RoundedCornerShape(16.dp))
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task", modifier = Modifier.size(32.dp))
            }
        }
    ) { innerPadding ->
        if (showBootyBag) {
            BootyBagDialog(
                onDismiss = { showBootyBag = false },
                onImportSelected = { selectedTasks ->
                    currentItems.addAll(selectedTasks.map { RoadmapItem.Task(it) })
                    showBootyBag = false
                },
                onCreateTask = onCreateTask
            )
        }

        if (showDurationDialog) {
            AddDurationDialog(
                onDismiss = { showDurationDialog = false },
                onConfirm = { time, isBefore ->
                    val item = RoadmapItem.Duration(time)
                    if (isBefore) {
                        currentItems.add(selectedTaskIndexForDuration, item)
                    } else {
                        currentItems.add(selectedTaskIndexForDuration + 1, item)
                    }
                    showDurationDialog = false
                }
            )
        }

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Dotted Background
            DottedBackground(color = lighterBrown)

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Header Badge
                Surface(
                    color = apricot,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(2.dp, brown)
                ) {
                    Text(
                        text = stringResource(R.string.roadmap_title_test1),
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                        color = brown,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Text(
                    text = stringResource(R.string.rm_editor_label),
                    color = brown,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp),
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Box(modifier = Modifier.fillMaxSize()) {
                    // Vertical Dashed Line
                    Canvas(modifier = Modifier
                        .fillMaxHeight()
                        .width(2.dp)
                        .align(Alignment.TopCenter)
                    ) {
                        drawLine(
                            color = brown,
                            start = Offset(0f, 0f),
                            end = Offset(0f, size.height),
                            strokeWidth = 4f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        )
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        itemsIndexed(currentItems) { index, item ->
                            when (item) {
                                is RoadmapItem.Task -> {
                                    EditorTaskCard(
                                        title = item.title,
                                        onDuplicate = {
                                            currentItems.add(index + 1, RoadmapItem.Task(item.title))
                                        },
                                        onDelete = {
                                            currentItems.removeAt(index)
                                        },
                                        onAddTime = {
                                            selectedTaskIndexForDuration = index
                                            showDurationDialog = true
                                        }
                                    )
                                }
                                is RoadmapItem.Duration -> {
                                    TimerBadge(time = item.time)
                                }
                            }
                        }
                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }
            }
        }
    }
}

sealed class RoadmapItem {
    data class Task(val title: String) : RoadmapItem()
    data class Duration(val time: String) : RoadmapItem()
}

@Composable
fun DottedBackground(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val dotRadius = 2f
        val spacing = 40f
        for (x in 0..(size.width / spacing).toInt()) {
            for (y in 0..(size.height / spacing).toInt()) {
                drawCircle(
                    color = color,
                    radius = dotRadius,
                    center = Offset(x * spacing, y * spacing)
                )
            }
        }
    }
}

@Composable
fun EditorTaskCard(
    title: String,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
    onAddTime: () -> Unit
) {
    val brown = colorResource(R.color.cigar)
    val cardBg = colorResource(R.color.cheesecake)
    val iconBg = colorResource(R.color.highlighter_blue)

    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .border(2.dp, brown, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBg)
                    .border(1.dp, brown, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Placeholder icons based on task
                val icon = when {
                    title.contains("bakery", ignoreCase = true) -> Icons.AutoMirrored.Filled.Login
                    title.contains("ovens", ignoreCase = true) -> Icons.Default.SettingsInputComponent
                    title.contains("loaves", ignoreCase = true) -> Icons.Default.BakeryDining
                    else -> Icons.AutoMirrored.Filled.Logout
                }
                Icon(icon, contentDescription = null, tint = brown, modifier = Modifier.size(28.dp))
            }

            Text(
                text = title,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                color = brown,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = brown,
                        modifier = Modifier.size(24.dp)
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier.background(cardBg).border(1.dp, brown, RoundedCornerShape(8.dp))
                ) {
                    DropdownMenuItem(
                        text = { Text("Duplicate Task", color = brown, fontWeight = FontWeight.Bold) },
                        onClick = {
                            onDuplicate()
                            showMenu = false
                        },
                        leadingIcon = { Icon(Icons.Default.ContentCopy, contentDescription = null, tint = brown) }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete Task", color = brown, fontWeight = FontWeight.Bold) },
                        onClick = {
                            onDelete()
                            showMenu = false
                        },
                        leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = brown) }
                    )
                    DropdownMenuItem(
                        text = { Text("Add Time", color = brown, fontWeight = FontWeight.Bold) },
                        onClick = {
                            onAddTime()
                            showMenu = false
                        },
                        leadingIcon = { Icon(Icons.Default.Timer, contentDescription = null, tint = brown) }
                    )
                }
            }
        }
    }
}

@Composable
fun TimerBadge(time: String) {
    val brown = colorResource(R.color.cigar)
    val gold = colorResource(R.color.gold)

    Surface(
        color = gold,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(2.dp, brown),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Timer, contentDescription = null, tint = brown, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = time, color = brown, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AddDurationDialog(onDismiss: () -> Unit, onConfirm: (String, Boolean) -> Unit) {
    val cigar = colorResource(R.color.cigar)
    val gold = colorResource(R.color.gold)
    val cream = colorResource(R.color.fresh_cream)
    val cheesecake = colorResource(R.color.cheesecake)

    var isBefore by remember { mutableStateOf(true) }
    var selectedHour by remember { mutableStateOf(3) }
    var selectedMin by remember { mutableStateOf(20) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(32.dp),
            color = cream,
            border = BorderStroke(3.dp, cigar)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(cigar)
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "ADD DURATION",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Toggle Tab
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .border(2.dp, cigar, RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .background(cheesecake),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(if (isBefore) gold else Color.Transparent)
                                .clickable { isBefore = true }
                                .border(
                                    if (isBefore) BorderStroke(2.dp, cigar) else BorderStroke(0.dp, Color.Transparent),
                                    RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "BEFORE TASK",
                                color = cigar,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(if (!isBefore) gold else Color.Transparent)
                                .clickable { isBefore = false }
                                .border(
                                    if (!isBefore) BorderStroke(2.dp, cigar) else BorderStroke(0.dp, Color.Transparent),
                                    RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "AFTER TASK",
                                color = cigar,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Time Picker Emulation
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .border(
                                BorderStroke(2.dp, cigar.copy(alpha = 0.3f)),
                                RoundedCornerShape(16.dp)
                            )
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Dashed background container for the "reels"
                        Canvas(modifier = Modifier.matchParentSize().padding(8.dp)) {
                            drawRect(
                                color = cigar.copy(alpha = 0.1f),
                                style = androidx.compose.ui.graphics.drawscope.Stroke(
                                    width = 2f,
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                                )
                            )
                        }

                        // Reel Picker Emulation
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TimeReel("HOURS", selectedHour, listOf(1, 2, 3, 4, 5)) { selectedHour = it }
                            Text(":", color = cigar, fontWeight = FontWeight.Bold, fontSize = 24.sp, modifier = Modifier.padding(horizontal = 24.dp))
                            TimeReel("MINS", selectedMin, listOf(10, 15, 20, 25, 30)) { selectedMin = it }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Confirm Button
                    Button(
                        onClick = {
                            val timeStr = if (selectedHour > 0) "${selectedHour}H ${selectedMin}M" else "${selectedMin} MIN"
                            onConfirm(timeStr, isBefore)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .border(3.dp, cigar, RoundedCornerShape(16.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = gold),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "CONFIRM TIME",
                            color = cigar,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TimeReel(label: String, selectedValue: Int, options: List<Int>, onSelect: (Int) -> Unit) {
    val cigar = colorResource(R.color.cigar)
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = cigar.copy(alpha = 0.4f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        
        options.forEach { value ->
            val isSelected = value == selectedValue
            Text(
                text = value.toString().padStart(2, '0'),
                color = if (isSelected) cigar else cigar.copy(alpha = 0.2f),
                fontSize = if (isSelected) 28.sp else 20.sp,
                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .clickable { onSelect(value) }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RoadMapEditorScreenPreview() {
    RoadMapEditorScreen()
}

@Composable
fun BootyBagDialog(onDismiss: () -> Unit, onImportSelected: (List<String>) -> Unit, onCreateTask: () -> Unit = {}) {
    val cigar = colorResource(R.color.cigar)
    val gold = colorResource(R.color.gold)
    val cream = colorResource(R.color.fresh_cream)
    val cheesecake = colorResource(R.color.cheesecake)

    var searchQuery by remember { mutableStateOf("") }
    val selectedTasks = remember { mutableStateListOf<Int>() }

    val bootyTasks = listOf(
        BootyTaskData("Bake Bread", "KITCHEN", Icons.Default.BakeryDining, colorResource(R.color.pale_apricot)),
        BootyTaskData("Restock Flour", "SUPPLY", Icons.AutoMirrored.Filled.Assignment, colorResource(R.color.highlighter_blue)),
        BootyTaskData("Scrub Decks", "CLEAN", Icons.Default.CleaningServices, colorResource(R.color.old_rose))
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp),
            shape = RoundedCornerShape(28.dp),
            color = cream,
            border = BorderStroke(3.dp, cigar)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(gold)
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = cigar)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "THE BOOTY BAG",
                                color = cigar,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(
                                onClick = onDismiss,
                                modifier = Modifier
                                    .size(32.dp)
                                    .border(1.dp, cigar, RoundedCornerShape(8.dp))
                                    .background(Color.White, RoundedCornerShape(8.dp))
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = cigar, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                    HorizontalDivider(color = cigar, thickness = 2.dp)
                }

                // Search
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .border(2.dp, cigar, RoundedCornerShape(12.dp))
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Scour the bag for tasks...", color = Color.LightGray, fontSize = 14.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = cigar) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        )
                    )
                }

                // Task List
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(bootyTasks) { index, task ->
                        val isSelected = selectedTasks.contains(index)
                        BootyTaskCard(
                            task = task,
                            isSelected = isSelected,
                            onToggle = {
                                if (isSelected) selectedTasks.remove(index) else selectedTasks.add(index)
                            }
                        )
                    }
                }

                // Footer
                Column(modifier = Modifier.fillMaxWidth()) {
                    HorizontalDivider(color = cigar, thickness = 2.dp)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(cheesecake)
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Create\nNew\nTask",
                                color = cigar,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                textDecoration = TextDecoration.Underline,
                                lineHeight = 14.sp,
                                modifier = Modifier.clickable { onCreateTask() }
                            )

                            Button(
                                onClick = {
                                    val selectedTitles = selectedTasks.map { bootyTasks[it].title }
                                    onImportSelected(selectedTitles)
                                },
                                modifier = Modifier
                                    .height(64.dp)
                                    .width(160.dp)
                                    .border(3.dp, cigar, RoundedCornerShape(16.dp)),
                                colors = ButtonDefaults.buttonColors(containerColor = gold),
                                shape = RoundedCornerShape(16.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    text = "IMPORT SELECTED",
                                    color = cigar,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

data class BootyTaskData(
    val title: String,
    val tag: String,
    val icon: ImageVector,
    val iconBg: Color
)

@Composable
fun BootyTaskCard(task: BootyTaskData, isSelected: Boolean, onToggle: () -> Unit) {
    val cigar = colorResource(R.color.cigar)
    val gold = colorResource(R.color.gold)
    
    val cardBg = if (isSelected) cigar else Color.White
    val textColor = if (isSelected) Color.White else cigar

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .border(2.dp, cigar, RoundedCornerShape(16.dp))
            .clickable { onToggle() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(task.iconBg)
                    .border(1.dp, cigar, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(task.icon, contentDescription = null, tint = cigar, modifier = Modifier.size(24.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Surface(
                    color = if (isSelected) Color.White.copy(alpha = 0.2f) else task.iconBg.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = task.tag,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        color = textColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Action Button
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) gold else Color.White)
                    .border(2.dp, cigar, CircleShape)
                    .clickable { onToggle() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isSelected) Icons.Default.Check else Icons.Default.Add,
                    contentDescription = null,
                    tint = cigar,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
