package a47514.masterplanner.ui

import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.foundation.lazy.rememberLazyListState
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import a47514.masterplanner.R

import a47514.masterplanner.Screen
import a47514.masterplanner.data.RoadmapViewModel
import a47514.masterplanner.data.Task
import a47514.masterplanner.data.Utility
import a47514.masterplanner.data.Utility.iconFromName
import a47514.masterplanner.ui.theme.LocalAppColors
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.toColorInt

@Composable
fun TaskLibraryScreen(
    roadmapViewModel: RoadmapViewModel,
    onCreateTask: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onNavigate: (Screen) -> Unit = {}
) {
    val colors = LocalAppColors.current
    val cream = colors.cream
    val brown = colors.brown
    val gold = colors.gold
    val cheesecake = colors.cheesecake
    val lighterBrown = colors.lighterBrown

    var searchQuery by remember { mutableStateOf("") }

    val libraryTasks by roadmapViewModel.libraryTasks.collectAsState()

    val taskList = remember { mutableStateListOf<Task>() }

    val context = LocalContext.current

    LaunchedEffect(libraryTasks) {
        val incomingIds = libraryTasks.map { it.id }
        val currentIds = taskList.map { it.id }
        if (incomingIds != currentIds) {
            taskList.clear()
            taskList.addAll(libraryTasks)
        }
    }

    val filteredTasks = remember(taskList.toList(), searchQuery) {
        if (searchQuery.isBlank()) taskList.toList()
        else taskList.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    val isDragEnabled = searchQuery.isBlank()

    val lazyListState = rememberLazyListState()
    val reorderState = rememberReorderableLazyListState(
        lazyListState = lazyListState,
        onMove = { from, to ->
            if (isDragEnabled) {
                // Offset by 3: spacer + search bar + section header
                val offset = 3
                val adjustedFrom = from.index - offset
                val adjustedTo = to.index - offset
                if (adjustedFrom >= 0 && adjustedTo >= 0
                    && adjustedFrom < taskList.size
                    && adjustedTo < taskList.size
                ) {
                    taskList.add(adjustedTo, taskList.removeAt(adjustedFrom))
                }
            }
        }
    )

    val isDragging = reorderState.isAnyItemDragging
    LaunchedEffect(isDragging) {
        if (!isDragging && taskList.isNotEmpty() && isDragEnabled) {
            roadmapViewModel.reorderLibraryTasks(taskList.toList())
        }
    }

    Scaffold(
        topBar = { TaskLibraryTopBar(onLogoutClick = onLogoutClick) },
        bottomBar = {
            MasterPlannerBottomBar(
                currentScreen = Screen.TaskLibrary,
                onNavigate = onNavigate
            )
        },
        containerColor = cream
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .drawBehind {
                    val dotRadius = 2f
                    val spacing = 40f
                    for (x in 0..(size.width / spacing).toInt()) {
                        for (y in 0..(size.height / spacing).toInt()) {
                            drawCircle(
                                color = lighterBrown,
                                radius = dotRadius,
                                center = Offset(x * spacing, y * spacing)
                            )
                        }
                    }
                }) {

            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(16.dp)) }

                // Search Bar
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .background(cheesecake.copy(alpha = 0.5f)),
                        placeholder = {
                            Text(
                                stringResource(R.string.task_lib_search_text),
                                color = lighterBrown
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = brown
                            )
                        },
                        shape = RoundedCornerShape(28.dp),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = lighterBrown,
                            focusedIndicatorColor = brown
                        )
                    )
                }

                // Section Header
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Master Tasks",
                            color = brown,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }

                items(filteredTasks, key = { it.id }) { task ->
                    ReorderableItem(
                        state = reorderState,
                        key = task.id,
                        enabled = isDragEnabled
                    ) { isDraggingItem ->
                        LibraryTaskCard(
                            task = task,
                            isDragging = isDraggingItem,
                            dragHandleModifier = if (isDragEnabled) Modifier.draggableHandle() else Modifier,
                            onDelete = {
                                roadmapViewModel.deleteLibraryTask(task.id) { success ->
                                    if (!success) Utility.showToast(context, "Delete failed")
                                }
                            }
                        )
                    }
                }

                // Footer Section
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // App Logo
                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                                .background(cheesecake)
                                .border(2.dp, brown, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.treasure_map_icon),
                                contentDescription = "App Logo",
                                modifier = Modifier.size(100.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "END OF LIBRARY",
                            color = lighterBrown,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            letterSpacing = 1.sp
                        )
                    }
                }

                item {
                    Button(
                        onClick = onCreateTask,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .border(2.dp, brown, RoundedCornerShape(32.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = gold),
                        shape = RoundedCornerShape(32.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.AddCircleOutline,
                                contentDescription = null,
                                tint = brown
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                stringResource(R.string.task_lib_new_task_button).uppercase(),
                                color = brown,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }
}

@Composable
fun LibraryTaskCard(
    task: Task,
    isDragging: Boolean = false,
    dragHandleModifier: Modifier = Modifier,
    onDelete: () -> Unit = {}
) {
    val colors = LocalAppColors.current
    val brown = colors.brown
    val lighterBrown = colors.lighterBrown
    val white = colors.white

    val elevation = if (isDragging) 12.dp else 2.dp
    val borderColor = if (isDragging) brown else lighterBrown
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .border(1.dp, borderColor, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = white),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.DragHandle,
                contentDescription = "Drag to reorder",
                tint = brown.copy(alpha = 0.35f),
                modifier = dragHandleModifier
                    .size(28.dp)
                    .padding(end = 4.dp)
            )

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(task.colorHex.toColorInt()))
                    .border(2.dp, lighterBrown, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    iconFromName(task.iconName), contentDescription = null,
                    tint = brown, modifier = Modifier.size(32.dp)
                )
            }

            Text(
                text = task.name,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                color = brown, fontWeight = FontWeight.Bold, fontSize = 18.sp
            )

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
                        .background(white)
                        .border(1.dp, lighterBrown, RoundedCornerShape(8.dp))
                ) {
                    DropdownMenuItem(
                        text = { Text("Delete Task", color = brown, fontWeight = FontWeight.Bold) },
                        onClick = { onDelete(); showMenu = false },
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

@Composable
fun TaskLibraryTopBar(onLogoutClick: () -> Unit = {}) {
    val colors = LocalAppColors.current
    val cigar = colors.cigar
    val cream = colors.cream

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
                .background(cigar)
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
                    .border(1.dp, cigar, RoundedCornerShape(8.dp))
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            "Logout",
                            color = cigar,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    onClick = {
                        showMenu = false
                        onLogoutClick()
                    },
                    leadingIcon = {
                        Icon(
                            Icons.AutoMirrored.Filled.Logout,
                            contentDescription = null,
                            tint = cigar
                        )
                    }
                )
            }
        }

        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = cigar,
            letterSpacing = 1.sp
        )
    }
}