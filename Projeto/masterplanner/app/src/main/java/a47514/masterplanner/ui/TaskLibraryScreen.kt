package a47514.masterplanner.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weathercompose.R

@Composable
fun TaskLibraryScreen() {
    val cream = colorResource(R.color.fresh_cream)
    val brown = colorResource(R.color.cigar)
    val gold = colorResource(R.color.gold)
    val blue = colorResource(R.color.highlighter_blue)
    val apricot = colorResource(R.color.pale_apricot)
    val cheesecake = colorResource(R.color.cheesecake)
    val lighterBrown = colorResource(R.color.old_rose)

    val task1 = stringResource(R.string.task_title_test1)
    val task2 = stringResource(R.string.task_title_test3)
    val task3 = stringResource(R.string.task_title_test5)
    val task4 = stringResource(R.string.task_title_test7)

    val tasks = remember {
        listOf(
            TaskItemData(task1, gold, Icons.Default.BakeryDining),
            TaskItemData(task2, blue, Icons.AutoMirrored.Filled.Assignment),
            TaskItemData(task3, apricot, Icons.Default.CleaningServices),
            TaskItemData(task4, gold, Icons.Default.MilitaryTech)
        )
    }

    Scaffold(
        topBar = { MasterPlannerTopBar() },
        bottomBar = { TaskLibraryBottomBar() },
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
                        Icon(Icons.Default.FilterList, contentDescription = "Filter", tint = brown)
                    }
                }

                items(tasks) { task ->
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
                        // Image placeholder
                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                                .background(lighterBrown)
                                .border(2.dp, lighterBrown, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AutoStories, contentDescription = null, tint = lighterBrown, modifier = Modifier.size(64.dp))
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
                        onClick = { /* TODO */ },
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

//@Composable
//fun LibraryTopBar() {
//    val brown = colorResource(R.color.cigar)
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp)
//            .statusBarsPadding(),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Icon(
//            Icons.AutoMirrored.Filled.ArrowBack,
//            contentDescription = "Back",
//            tint = brown,
//            modifier = Modifier.size(28.dp)
//        )
//
//        Text(
//            text = stringResource(R.string.task_lib_title).uppercase(),
//            style = MaterialTheme.typography.titleLarge,
//            fontWeight = FontWeight.ExtraBold,
//            color = brown,
//            letterSpacing = 1.sp
//        )
//
//        Box(
//            modifier = Modifier
//                .size(36.dp)
//                .clip(CircleShape)
//                .background(brown)
//        ) {
//            Icon(
//                Icons.Default.Person,
//                contentDescription = null,
//                tint = Color.White,
//                modifier = Modifier.size(24.dp).align(Alignment.Center)
//            )
//        }
//    }
//}

data class TaskItemData(val title: String, val color: Color, val icon: ImageVector)

@Composable
fun LibraryTaskCard(task: TaskItemData) {
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
                    .background(task.color)
                    .border(2.dp, lighterBrown, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(task.icon, contentDescription = null, tint = brown, modifier = Modifier.size(32.dp))
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = task.title,
                    color = brown,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun TaskLibraryBottomBar() {
    val brown = colorResource(R.color.cigar)
    val lighterBrown = colorResource(R.color.old_rose)
    val cream = colorResource(R.color.fresh_cream)
    val gold = colorResource(R.color.gold)

    NavigationBar(
        containerColor = cream,
        tonalElevation = 8.dp,
        modifier = Modifier.clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
    ) {
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Map, contentDescription = null) },
            label = { Text("ROADMAPS") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = lighterBrown,
                selectedTextColor = lighterBrown
            )
        )
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Icon(Icons.Default.LibraryBooks, contentDescription = null) },
            label = { Text("LIBRARY") },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = brown,
                unselectedTextColor = brown,
                indicatorColor = gold
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
            label = { Text("SETTINGS") },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = lighterBrown,
                unselectedTextColor = lighterBrown
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TaskLibraryScreenPreview() {
    TaskLibraryScreen()
}
