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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import a47514.masterplanner.R

import a47514.masterplanner.Screen
import a47514.masterplanner.data.RoadmapViewModel
import android.widget.NumberPicker
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.toColorInt

@Composable
fun RoadMapEditorScreen(
    roadmapId: String,
    roadmapViewModel: RoadmapViewModel,
    onCreateTask: () -> Unit = {},
    onNavigate: (Screen) -> Unit = {},
    onBack: () -> Unit = {},
    onFreemium: () -> Unit = {}
) {
    val tasks by roadmapViewModel.tasks.collectAsState()
    val currentRoadmap by roadmapViewModel.currentRoadmap.collectAsState()

    val cream = colorResource(R.color.fresh_cream)
    val brown = colorResource(R.color.cigar)
    val gold = colorResource(R.color.gold)
    val lighterBrown = colorResource(R.color.old_rose)

    var showBootyBag by remember { mutableStateOf(false) }
    var showDurationDialog by remember { mutableStateOf(false) }
    var selectedTaskIndexForDuration by remember { mutableIntStateOf(-1) }

    // Load persisted order+durations from Firestore, fall back to tasks-only if empty
    val currentItems = remember(currentRoadmap, tasks) {
        val entries = currentRoadmap?.itemEntries ?: emptyList()
        if (entries.isNotEmpty()) {
            entries.map { entry ->
                if (entry.type == "duration") {
                    RoadmapItem.Duration(entry.durationLabel) as RoadmapItem
                } else {
                    RoadmapItem.Task(
                        title = entry.taskName,
                        taskId = entry.taskId,
                        iconName = entry.iconName,
                        colorHex = entry.colorHex
                    ) as RoadmapItem
                }
            }.toMutableStateList()
        } else {
            // No saved order yet — use tasks sorted by timestamp as baseline
            tasks.map { task ->
                RoadmapItem.Task(
                    title = task.name,
                    taskId = task.id,
                    iconName = task.iconName,
                    colorHex = task.colorHex
                ) as RoadmapItem
            }.toMutableStateList()
        }
    }

    Scaffold(
        topBar = {
            RoadMapEditorTopBar(
                title = currentRoadmap?.title ?: "...",
                onSaveAndBack = {
                    roadmapViewModel.saveRoadmapItems(roadmapId, currentItems.toList()) {
                        onBack()
                    }
                }
            )
        },
        bottomBar = { 
            MasterPlannerBottomBar(
                currentScreen = Screen.RoadMapEditor,
                onNavigate = onNavigate
            )
        },
        containerColor = cream,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (currentItems.count { it is RoadmapItem.Task } >= 6) {
                        onFreemium()
                    } else {
                        showBootyBag = true
                    }
                },
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
                roadmapViewModel = roadmapViewModel,
                onDismiss = { showBootyBag = false },
                onImportSelected = { selectedTasks ->
                    val taskCount = currentItems.count { it is RoadmapItem.Task }
                    val allowed = selectedTasks.take((6 - taskCount).coerceAtLeast(0))
                    if (selectedTasks.size > allowed.size) onFreemium()
                    if (allowed.isNotEmpty()) {
                        currentItems.addAll(allowed)
                        showBootyBag = false
                        roadmapViewModel.saveRoadmapItems(roadmapId, currentItems.toList()) {}
                    }
                },
                onCreateTask = onCreateTask
            )
        }

        if (showDurationDialog) {
            AddDurationDialog(
                onDismiss = { showDurationDialog = false },
                onConfirm = { time, isBefore ->
                    val item = RoadmapItem.Duration(time)
                    if (isBefore) currentItems.add(selectedTaskIndexForDuration, item)
                    else currentItems.add(selectedTaskIndexForDuration + 1, item)
                    showDurationDialog = false
                    roadmapViewModel.saveRoadmapItems(roadmapId, currentItems.toList()) {}
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
                                        iconName = item.iconName,
                                        colorHex = item.colorHex,
                                        onDuplicate = {
                                            currentItems.add(index + 1, RoadmapItem.Task(item.title, iconName = item.iconName, colorHex = item.colorHex))
                                        },
                                        onDelete = {
                                            val taskId = (currentItems[index] as? RoadmapItem.Task)?.taskId ?: ""
                                            roadmapViewModel.deleteTask(roadmapId, taskId) {}
                                            currentItems.removeAt(index)
                                            roadmapViewModel.saveRoadmapItems(roadmapId, currentItems.toList()) {}
                                        },
                                        onAddTime = {
                                            selectedTaskIndexForDuration = index
                                            showDurationDialog = true
                                        }
                                    )
                                }
                                is RoadmapItem.Duration -> {
                                    TimerBadge(
                                        time = item.time,
                                        onDelete = {
                                            currentItems.removeAt(index)
                                            roadmapViewModel.saveRoadmapItems(roadmapId, currentItems.toList()) {}
                                        }
                                    )
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
    data class Task(
        val title: String,
        val taskId: String = "",
        val iconName: String = "",
        val colorHex: String = ""
    ) : RoadmapItem()
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
    iconName: String = "",
    colorHex: String = "",
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
    onAddTime: () -> Unit
) {
    val brown = colorResource(R.color.cigar)
    val cardBg = colorResource(R.color.cheesecake)
    val iconBgColor = try {
        Color(colorHex.toColorInt())
    } catch (e: Exception) {
        colorResource(R.color.highlighter_blue)
    }

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
                    .background(iconBgColor)
                    .border(1.dp, brown, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = a47514.masterplanner.data.Utility.iconFromName(iconName),
                    contentDescription = null,
                    tint = brown,
                    modifier = Modifier.size(28.dp)
                )
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
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = brown, modifier = Modifier.size(24.dp))
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier.background(cardBg).border(1.dp, brown, RoundedCornerShape(8.dp))
                ) {
                    DropdownMenuItem(
                        text = { Text("Duplicate Task", color = brown, fontWeight = FontWeight.Bold) },
                        onClick = { onDuplicate(); showMenu = false },
                        leadingIcon = { Icon(Icons.Default.ContentCopy, contentDescription = null, tint = brown) }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete Task", color = brown, fontWeight = FontWeight.Bold) },
                        onClick = { onDelete(); showMenu = false },
                        leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = brown) }
                    )
                    DropdownMenuItem(
                        text = { Text("Add Time", color = brown, fontWeight = FontWeight.Bold) },
                        onClick = { onAddTime(); showMenu = false },
                        leadingIcon = { Icon(Icons.Default.Timer, contentDescription = null, tint = brown) }
                    )
                }
            }
        }
    }
}

@Composable
fun TimerBadge(time: String, onDelete: () -> Unit = {}) {
    val brown = colorResource(R.color.cigar)
    val gold = colorResource(R.color.gold)

    Surface(
        color = gold,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(2.dp, brown),
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable { onDelete() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Timer, contentDescription = null,
                tint = brown, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = time, color = brown, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Default.Close, contentDescription = "Remove",
                tint = brown.copy(alpha = 0.6f), modifier = Modifier.size(14.dp))
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
    var selectedHour by remember { mutableIntStateOf(3) }
    var selectedMin by remember { mutableIntStateOf(20) }

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

                        // Wheel Picker Integration
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("HOURS", color = cigar.copy(alpha = 0.6f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                HourPicker(value = selectedHour, onValueChange = { selectedHour = it })
                            }
                            
                            Text(":", color = cigar, fontWeight = FontWeight.Bold, fontSize = 32.sp, modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp))

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("MINS", color = cigar.copy(alpha = 0.6f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                MinutePicker(value = selectedMin, onValueChange = { selectedMin = it })
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Confirm Button
                    Button(
                        onClick = {
                            val timeStr = if (selectedHour > 0) "${selectedHour}H ${selectedMin}M" else "$selectedMin MIN"
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
fun HourPicker(
    value: Int,
    onValueChange: (Int) -> Unit
) {
    AndroidView(
        factory = { context ->
            NumberPicker(context).apply {
                minValue = 0
                maxValue = 23
                wrapSelectorWheel = true

                setOnValueChangedListener { _, _, newVal ->
                    onValueChange(newVal)
                }
            }
        },
        update = {
            it.value = value
        }
    )
}

@Composable
fun MinutePicker(
    value: Int,
    onValueChange: (Int) -> Unit
) {
    AndroidView(
        factory = { context ->
            NumberPicker(context).apply {
                minValue = 0
                maxValue = 59
                wrapSelectorWheel = true

                setOnValueChangedListener { _, _, newVal ->
                    onValueChange(newVal)
                }
            }
        },
        update = {
            it.value = value
        }
    )
}

@Composable
fun BootyBagDialog(
    roadmapViewModel: RoadmapViewModel,
    onDismiss: () -> Unit,
    onImportSelected: (List<RoadmapItem.Task>) -> Unit,
    onCreateTask: () -> Unit = {}
) {
    val cigar = colorResource(R.color.cigar)
    val gold = colorResource(R.color.gold)
    val cream = colorResource(R.color.fresh_cream)
    val cheesecake = colorResource(R.color.cheesecake)

    var searchQuery by remember { mutableStateOf("") }
    val selectedTaskIds = remember { mutableStateListOf<String>() }

    val libraryTasks by roadmapViewModel.libraryTasks.collectAsState()
    val filteredTasks = remember(libraryTasks, searchQuery) {
        if (searchQuery.isBlank()) libraryTasks
        else libraryTasks.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    LaunchedEffect(Unit) { roadmapViewModel.listenToTaskLibrary() }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth().height(600.dp),
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
                            Text("THE BOOTY BAG", color = cigar, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
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
                if (filteredTasks.isEmpty()) {
                    Box(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (libraryTasks.isEmpty()) "No tasks in your library yet.\nCreate one first!" else "No tasks match your search.",
                            color = cigar.copy(alpha = 0.5f),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item { Spacer(modifier = Modifier.height(4.dp)) }
                        items(filteredTasks) { task ->
                            val isSelected = selectedTaskIds.contains(task.id)
                            val iconBgColor = try {
                                Color(task.colorHex.toColorInt())
                            } catch (e: Exception) { Color(0xFFFFD700) }

                            BootyTaskCard(
                                task = BootyTaskData(
                                    title = task.name,
                                    tag = task.iconName.uppercase(),
                                    icon = a47514.masterplanner.data.Utility.iconFromName(task.iconName),
                                    iconBg = iconBgColor
                                ),
                                isSelected = isSelected,
                                onToggle = {
                                    if (isSelected) selectedTaskIds.remove(task.id)
                                    else selectedTaskIds.add(task.id)
                                }
                            )
                        }
                        item { Spacer(modifier = Modifier.height(4.dp)) }
                    }
                }

                // Footer
                Column(modifier = Modifier.fillMaxWidth()) {
                    HorizontalDivider(color = cigar, thickness = 2.dp)
                    Box(
                        modifier = Modifier.fillMaxWidth().background(cheesecake).padding(16.dp)
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
                                    val selected = filteredTasks
                                        .filter { selectedTaskIds.contains(it.id) }
                                        .map { task ->
                                            RoadmapItem.Task(
                                                title = task.name,
                                                taskId = task.id,
                                                iconName = task.iconName,
                                                colorHex = task.colorHex
                                            )
                                        }
                                    onImportSelected(selected)
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
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
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
fun RoadMapEditorTopBar(
    title: String,
    onSaveAndBack: () -> Unit
) {
    val brown = colorResource(R.color.cigar)
    val cream = colorResource(R.color.fresh_cream)
    val gold = colorResource(R.color.gold)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Back + Save button
        IconButton(
            onClick = onSaveAndBack,
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(brown)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Save & Back",
                tint = cream,
                modifier = Modifier.size(22.dp)
            )
        }

        // Roadmap title badge — centered
        Surface(
            color = gold,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(2.dp, brown),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = title,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                color = brown,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }

        // Placeholder to balance the row (same size as back button)
        Spacer(modifier = Modifier.size(44.dp))
    }
}

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
