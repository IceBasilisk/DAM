package a47514.masterplanner.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun CreateTaskScreen(onBack: () -> Unit = {}) {
    val cream = colorResource(R.color.fresh_cream)
    val cigar = colorResource(R.color.cigar)
    val gold = colorResource(R.color.gold)
    val cheesecake = colorResource(R.color.cheesecake)

    var taskName by remember { mutableStateOf("") }
    var selectedMark by remember { mutableIntStateOf(0) }
    var selectedColor by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            ForgeTaskTopBar(onBack = onBack)
        },
        bottomBar = {
            ForgeTaskBottomBar()
        },
        containerColor = cream
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // LOG ENTRY NAME
            Text(
                text = stringResource(R.string.task_create_title_label),
                color = cigar,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            PirateTextField(
                value = taskName,
                onValueChange = { taskName = it },
                placeholder = stringResource(R.string.task_create_title_example)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // CHOOSE THY MARK
            Text(
                text = stringResource(R.string.task_create_icon_label),
                color = cigar,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                val newRow = listOf(
                    "Waves" to Icons.Default.Waves,
                    "Flag" to Icons.Default.Flag,
                    "Ship" to Icons.Default.DirectionsBoat,
                    "Gem" to Icons.Default.Diamond
                )
                val originalRow = listOf(
                    "Compass" to Icons.Default.Explore,
                    "Anchor" to Icons.Default.Anchor,
                    "Sails" to Icons.Default.Sailing,
                    "Chest" to Icons.Default.Inventory
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    newRow.forEachIndexed { index, pair ->
                        MarkItem(
                            label = pair.first,
                            icon = pair.second,
                            isSelected = selectedMark == index,
                            onClick = { selectedMark = index }
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    originalRow.forEachIndexed { index, pair ->
                        val globalIndex = index + 4
                        MarkItem(
                            label = pair.first,
                            icon = pair.second,
                            isSelected = selectedMark == globalIndex,
                            onClick = { selectedMark = globalIndex }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // GUILD COLORS
            Text(
                text = stringResource(R.string.task_create_color_label),
                color = cigar,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            ColorSelectorCard(
                selectedColorIndex = selectedColor,
                onColorSelect = { selectedColor = it }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Create Task Button
            PirateButton(
                text = stringResource(R.string.task_create_button_text),
                icon = Icons.Default.AutoFixHigh,
                onClick = { /* Handle creation */ }
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun ForgeTaskTopBar(onBack: () -> Unit) {
    val cigar = colorResource(R.color.cigar)
    val cream = colorResource(R.color.fresh_cream)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .statusBarsPadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onBack) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = cigar,
                modifier = Modifier.size(28.dp)
            )
        }

        Text(
            text = "Forge New Task",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = cigar,
            letterSpacing = 0.5.sp
        )

        // Account Icon (Right side as requested, same style as RoadMapEditorScreen)
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(cigar)
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = "Account",
                tint = cream,
                modifier = Modifier.align(Alignment.Center).size(24.dp)
            )
        }
    }
}

@Composable
fun ForgeTaskBottomBar() {
    val brown = colorResource(R.color.cigar)
    val cream = colorResource(R.color.fresh_cream)
    val gold = colorResource(R.color.gold)
    val lighterBrown = colorResource(R.color.old_rose)

    Column {
        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp)
        NavigationBar(
            containerColor = cream,
            tonalElevation = 0.dp,
            modifier = Modifier.clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
        ) {
            NavigationBarItem(
                selected = false,
                onClick = { },
                icon = { Icon(Icons.Default.Map, contentDescription = null) },
                label = { Text("Map") },
                colors = NavigationBarItemDefaults.colors(
                    unselectedIconColor = lighterBrown,
                    unselectedTextColor = lighterBrown
                )
            )
            NavigationBarItem(
                selected = true,
                onClick = { },
                icon = { Icon(Icons.Default.Hardware, contentDescription = null) },
                label = { Text("Forge") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = brown,
                    selectedTextColor = brown,
                    indicatorColor = gold
                )
            )
            NavigationBarItem(
                selected = false,
                onClick = { },
                icon = { Icon(Icons.Default.Lock, contentDescription = null) },
                label = { Text("Vault") },
                colors = NavigationBarItemDefaults.colors(
                    unselectedIconColor = lighterBrown,
                    unselectedTextColor = lighterBrown
                )
            )
            NavigationBarItem(
                selected = false,
                onClick = { },
                icon = { Icon(Icons.Default.People, contentDescription = null) },
                label = { Text("Crew") },
                colors = NavigationBarItemDefaults.colors(
                    unselectedIconColor = lighterBrown,
                    unselectedTextColor = lighterBrown
                )
            )
        }
    }
}

@Composable
fun PirateTextField(value: String, onValueChange: (String) -> Unit, placeholder: String) {
    val cigar = colorResource(R.color.cigar)
    val cheesecake = colorResource(R.color.cheesecake)

    Box(modifier = Modifier.fillMaxWidth()) {
        // Hard Shadow
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = 6.dp, y = 6.dp)
                .background(cigar, RoundedCornerShape(12.dp))
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = Color.Gray.copy(alpha = 0.6f)) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = cheesecake,
                unfocusedContainerColor = cheesecake,
                focusedBorderColor = cigar,
                unfocusedBorderColor = cigar
            ),
            singleLine = true
        )
    }
}

@Composable
fun MarkItem(label: String, icon: ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    val cigar = colorResource(R.color.cigar)
    val cheesecake = colorResource(R.color.cheesecake)
    val gold = colorResource(R.color.gold)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clickable { onClick() }
        ) {
            // Hard Shadow
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .offset(x = 5.dp, y = 5.dp)
                    .background(cigar, RoundedCornerShape(12.dp))
            )
            // Foreground
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(if (isSelected) gold else cheesecake, RoundedCornerShape(12.dp))
                    .border(
                        width = if (isSelected) 3.dp else 2.dp,
                        color = cigar,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = label, tint = cigar, modifier = Modifier.size(32.dp))
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, color = cigar, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ColorSelectorCard(selectedColorIndex: Int, onColorSelect: (Int) -> Unit) {
    val cigar = colorResource(R.color.cigar)
    val cheesecake = colorResource(R.color.cheesecake)

    val colors = listOf(
        colorResource(R.color.gold),
        colorResource(R.color.sea_blue),
        colorResource(R.color.salsa_red),
        colorResource(R.color.judge_gray)
    )

    Box(modifier = Modifier.fillMaxWidth().height(80.dp)) {
        // Hard Shadow
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = 6.dp, y = 6.dp)
                .background(cigar, RoundedCornerShape(16.dp))
        )
        // Card
        Row(
            modifier = Modifier
                .matchParentSize()
                .background(cheesecake, RoundedCornerShape(16.dp))
                .border(2.dp, cigar, RoundedCornerShape(16.dp))
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            colors.forEachIndexed { index, color ->
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            width = if (selectedColorIndex == index) 4.dp else 2.dp,
                            color = cigar,
                            shape = CircleShape
                        )
                        .clickable { onColorSelect(index) }
                )
            }
        }
    }
}

@Composable
fun PirateButton(text: String, icon: ImageVector, onClick: () -> Unit) {
    val cigar = colorResource(R.color.cigar)
    val gold = colorResource(R.color.gold)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable { onClick() }
    ) {
        // Hard Shadow
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = 6.dp, y = 6.dp)
                .background(cigar, RoundedCornerShape(16.dp))
        )
        // Button
        Surface(
            modifier = Modifier.matchParentSize(),
            color = gold,
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(3.dp, cigar)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon, contentDescription = null, tint = cigar, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = text,
                    color = cigar,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun CreateTaskScreenPreview() {
    CreateTaskScreen()
}
