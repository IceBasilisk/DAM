package a47514.masterplanner.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import a47514.masterplanner.ui.theme.LocalAppColors

@Composable
fun FreemiumScreen(
    onDismiss: () -> Unit = {}
) {
    val colors = LocalAppColors.current
    val brown = colors.brown
    val cream = colors.cream
    val gold = colors.gold
    val cheesecake = colors.cheesecake

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF432818), Color(0xFF8B4513))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Treasure chest icon
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(gold),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = null,
                    tint = brown,
                    modifier = Modifier.size(52.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "YOU'VE HIT THE\nFREE LIMIT",
                color = gold,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp,
                lineHeight = 34.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Even the bravest pirates need a full crew.",
                color = cream.copy(alpha = 0.7f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Free tier card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = cheesecake.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, cream.copy(alpha = 0.2f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("⚓  FREE TIER",
                        color = cream.copy(alpha = 0.5f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    FreemiumFeatureRow("Up to 3 Roadmaps", true, cream)
                    FreemiumFeatureRow("Up to 6 Tasks per Roadmap", true, cream)
                    FreemiumFeatureRow("Task Library", true, cream)
                    FreemiumFeatureRow("AI Task Suggestions", false, cream)
                    FreemiumFeatureRow("Unlimited Roadmaps", false, cream)
                    FreemiumFeatureRow("Unlimited Tasks", false, cream)
                    FreemiumFeatureRow("Background Music", false, cream)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Premium card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = gold.copy(alpha = 0.15f),
                border = BorderStroke(2.dp, gold)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("⚔  CAPTAIN'S EDITION",
                            color = gold,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 2.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = gold,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text("RECOMMENDED",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                color = brown,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    FreemiumFeatureRow("Unlimited Roadmaps", true, gold)
                    FreemiumFeatureRow("Unlimited Tasks", true, gold)
                    FreemiumFeatureRow("AI Task Suggestions", true, gold)
                    FreemiumFeatureRow("Background Music Controls", true, gold)
                    FreemiumFeatureRow("Priority Support", true, gold)

                    Spacer(modifier = Modifier.height(20.dp))

                    // Price
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text("$4.99",
                            color = gold,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Black)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("/ month",
                            color = gold.copy(alpha = 0.7f),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 6.dp))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Upgrade button
                    Button(
                        onClick = { /* TODO: hook up billing */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .border(2.dp, brown, RoundedCornerShape(14.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = gold),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null,
                            tint = brown, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("CLAIM YOUR TREASURE",
                            color = brown,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Dismiss
            TextButton(onClick = onDismiss) {
                Text(
                    "Maybe later, keep sailing free",
                    color = cream.copy(alpha = 0.5f),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun FreemiumFeatureRow(label: String, included: Boolean, tintColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (included) Icons.Default.Check else Icons.Default.Close,
            contentDescription = null,
            tint = if (included) tintColor else tintColor.copy(alpha = 0.3f),
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = label,
            color = if (included) tintColor else tintColor.copy(alpha = 0.3f),
            fontSize = 14.sp,
            fontWeight = if (included) FontWeight.Medium else FontWeight.Normal
        )
    }
}