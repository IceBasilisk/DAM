package a47514.masterplanner.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weathercompose.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit = {}) {
    val cream = colorResource(R.color.fresh_cream)
    val cigar = colorResource(R.color.cigar)
    val gold = colorResource(R.color.gold)

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
        
        delay(2500) // Total splash duration
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
                    .background(Color.White, CircleShape)
                    .border(5.dp, cigar, CircleShape)
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
                text = "MASTER PLANNER",
                color = cigar,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Black,
                letterSpacing = 8.sp,
                modifier = Modifier.scale(entryScale.value)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Slogan
            Text(
                text = "CHART YOUR DESTINY",
                color = cigar.copy(alpha = 0.6f),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 4.sp,
                modifier = Modifier.scale(entryScale.value)
            )
            
            Spacer(modifier = Modifier.height(64.dp))
            
            // Loading indicator that matches the theme
            LinearProgressIndicator(
                modifier = Modifier
                    .width(140.dp)
                    .height(8.dp)
                    .clip(CircleShape)
                    .border(1.dp, cigar.copy(alpha = 0.2f), CircleShape),
                color = gold,
                trackColor = cigar.copy(alpha = 0.1f)
            )
        }
        
        // Version info at the bottom
        Text(
            text = "v1.0 - THE GOLDEN AGE",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp),
            color = cigar.copy(alpha = 0.3f),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}
