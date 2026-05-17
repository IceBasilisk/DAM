package a47514.masterplanner.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weathercompose.R

@Composable
fun RoadMapEditorScreen() {
    val cream = colorResource(R.color.fresh_cream)
    val brown = colorResource(R.color.cigar)
    val gold = colorResource(R.color.gold)
    val apricot = colorResource(R.color.pale_apricot)
    val lighterBrown = colorResource(R.color.old_rose)

    Scaffold(
        topBar = { MasterPlannerTopBar() },
        bottomBar = { MainMenuBottomBar() },
        containerColor = cream,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO */ },
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

                val tasks = listOf(
                    stringResource(R.string.task_title_test1),
                    stringResource(R.string.task_title_test2),
                    stringResource(R.string.task_title_test3),
                    stringResource(R.string.task_title_test4),
                    stringResource(R.string.task_title_test5),
                    stringResource(R.string.task_title_test6)
                )

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
                        itemsIndexed(tasks) { index, task ->
                            if (index == 3) {
                                // Timer Badge
                                TimerBadge(time = stringResource(R.string.duration_test1))
                            }
                            EditorTaskCard(title = task)
                        }
                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }
            }
        }
    }
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

//@Composable
//fun EditorTopBar() {
//    val brown = colorResource(R.color.cigar)
//    val cream = colorResource(R.color.fresh_cream)
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp)
//            .statusBarsPadding(),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        // Pirate Avatar Placeholder
//        Box(
//            modifier = Modifier
//                .size(40.dp)
//                .clip(CircleShape)
//                .background(brown)
//                .border(2.dp, brown, CircleShape)
//        ) {
//            Icon(
//                Icons.Default.Person,
//                contentDescription = null,
//                tint = cream,
//                modifier = Modifier.align(Alignment.Center)
//            )
//        }
//
//        Text(
//            text = "MASTER PLANNER",
//            style = MaterialTheme.typography.titleLarge,
//            fontWeight = FontWeight.ExtraBold,
//            color = brown,
//            letterSpacing = 1.sp
//        )
//
//        Row {
//            Icon(
//                Icons.Default.Search,
//                contentDescription = "Search",
//                tint = brown,
//                modifier = Modifier.size(28.dp)
//            )
//            Spacer(modifier = Modifier.width(16.dp))
//            Icon(
//                Icons.Default.Notifications,
//                contentDescription = "Notifications",
//                tint = brown,
//                modifier = Modifier.size(28.dp)
//            )
//        }
//    }
//}

@Composable
fun EditorTaskCard(title: String) {
    val brown = colorResource(R.color.cigar)
    val cardBg = colorResource(R.color.cheesecake)
    val iconBg = colorResource(R.color.highlighter_blue)

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

            Icon(
                Icons.Default.Edit,
                contentDescription = "Edit",
                tint = brown,
                modifier = Modifier.size(24.dp)
            )
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

@Preview(showBackground = true)
@Composable
fun RoadMapEditorScreenPreview() {
    RoadMapEditorScreen()
}
