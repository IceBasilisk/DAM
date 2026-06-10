package a47514.masterplanner.ui

import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import a47514.masterplanner.R

import a47514.masterplanner.Screen
import a47514.masterplanner.data.RoadmapViewModel
import a47514.masterplanner.data.Task
import a47514.masterplanner.data.Utility.iconFromName
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.graphics.toColorInt

@Composable
fun TaskLibraryScreen(
    roadmapViewModel: RoadmapViewModel,
    onCreateTask: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onNavigate: (Screen) -> Unit = {}
) {
    val cream = colorResource(R.color.fresh_cream)
    val brown = colorResource(R.color.cigar)
    val gold = colorResource(R.color.gold)
    val cheesecake = colorResource(R.color.cheesecake)
    val lighterBrown = colorResource(R.color.old_rose)

    val libraryTasks by roadmapViewModel.libraryTasks.collectAsState()

    LaunchedEffect(Unit) { roadmapViewModel.listenToTaskLibrary() }

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
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            DottedBackground(color = lighterBrown)

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(16.dp)) }

                // Search Bar
                item {
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .background(cheesecake.copy(alpha = 0.5f)),
                        placeholder = { Text(stringResource(R.string.task_lib_search_text), color = lighterBrown) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = brown) },
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
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
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

                items(libraryTasks) { task ->
                    LibraryTaskCard(task)
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
                            Icon(Icons.Default.AddCircleOutline, contentDescription = null, tint = brown)
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

data class TaskItemData(val title: String, val color: Color, val icon: ImageVector)

@Composable
fun LibraryTaskCard(task: Task) {
    val brown = colorResource(R.color.cigar)
    val cardBg = Color.White
    val lighterBrown = colorResource(R.color.old_rose)


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .border(1.dp, lighterBrown, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(task.colorHex.toColorInt()))
                    .border(2.dp, lighterBrown, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(iconFromName(task.iconName), contentDescription = null, tint = brown, modifier = Modifier.size(32.dp))
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = task.name,
                    color = brown,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun TaskLibraryTopBar(onLogoutClick: () -> Unit = {}) {
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
    }
}