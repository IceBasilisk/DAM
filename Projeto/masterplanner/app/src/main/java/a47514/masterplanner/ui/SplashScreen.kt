package a47514.masterplanner.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import a47514.masterplanner.ui.theme.LocalAppColors
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import a47514.masterplanner.R
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import androidx.compose.ui.text.style.TextAlign

@Composable
fun SplashScreen(onTimeout: () -> Unit = {}) {
    val colors = LocalAppColors.current
    val cream = colors.cream
    val cigar = colors.cigar

    // Pulse animation for the logo
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    // Entry animation (Scale and Alpha)
    val entryScale = remember { Animatable(0.5f) }
    val entryAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Start entry animations
        entryScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        entryAlpha.animateTo(1f, tween(1000))
        
        delay(2500.milliseconds) // Total splash duration
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(cream),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // The Treasure Map Icon with a themed border and shadow
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .scale(entryScale.value * pulseScale)
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.treasure_map_icon),
                    contentDescription = "Master Planner Logo",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Brand Name with Pirate Aesthetic
            Text(
                text = stringResource(R.string.app_name).uppercase(),
                color = cigar,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Black,
                letterSpacing = 6.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(entryScale.value)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Slogan
            Text(
                text = stringResource(R.string.slogan),
                color = cigar.copy(alpha = 0.6f),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 4.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(entryScale.value)
            )
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}
