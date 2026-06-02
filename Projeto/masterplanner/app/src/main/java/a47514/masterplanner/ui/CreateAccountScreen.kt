package a47514.masterplanner.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weathercompose.R

@Composable
fun CreateAccountScreen() {
    val cream = colorResource(R.color.fresh_cream)
    val cigar = colorResource(R.color.cigar)
    val gold = colorResource(R.color.gold)
    val cheesecake = colorResource(R.color.cheesecake)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(cream)
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Ship Icon Placeholder (Top Circle)
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .shadow(8.dp, CircleShape)
                    .background(Color.White, CircleShape)
                    .border(4.dp, Color.Gray.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // In a real app, use an Image here. Using Icon as placeholder.
                Icon(
                    imageVector = Icons.Default.DirectionsBoat,
                    contentDescription = null,
                    tint = cigar,
                    modifier = Modifier.size(70.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Main Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                color = cheesecake,
                shadowElevation = 12.dp,
                border = androidx.compose.foundation.BorderStroke(3.dp, cigar)
            ) {
                Column(
                    modifier = Modifier
                        .padding(28.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.signup_intro),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = cigar,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = stringResource(R.string.signup_intro_flavor_text),
                        color = cigar.copy(alpha = 0.8f),
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Email Field
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Email",
                            fontWeight = FontWeight.Bold,
                            color = cigar,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(
                                    stringResource(R.string.signup_email_text),
                                    color = Color.LightGray
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Email,
                                    contentDescription = null,
                                    tint = cigar
                                )
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = cream,
                                unfocusedContainerColor = cream,
                                focusedBorderColor = cigar,
                                unfocusedBorderColor = cigar.copy(alpha = 0.5f)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Password Field
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Password",
                            fontWeight = FontWeight.Bold,
                            color = cigar,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(
                                    stringResource(R.string.signup_password_text),
                                    color = Color.LightGray
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.VpnKey,
                                    contentDescription = null,
                                    tint = cigar
                                )
                            },
                            shape = RoundedCornerShape(16.dp),
                            visualTransformation = PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = cream,
                                unfocusedContainerColor = cream,
                                focusedBorderColor = cigar,
                                unfocusedBorderColor = cigar.copy(alpha = 0.5f)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Confirm Password Field
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Confirm Password",
                            fontWeight = FontWeight.Bold,
                            color = cigar,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(
                                    stringResource(R.string.signup_password_text),
                                    color = Color.LightGray
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.VpnKey,
                                    contentDescription = null,
                                    tint = cigar
                                )
                            },
                            shape = RoundedCornerShape(16.dp),
                            visualTransformation = PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = cream,
                                unfocusedContainerColor = cream,
                                focusedBorderColor = cigar,
                                unfocusedBorderColor = cigar.copy(alpha = 0.5f)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Enlist Now Button
                    Button(
                        onClick = { /* TODO */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .shadow(4.dp, RoundedCornerShape(16.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = gold),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(4.dp, cigar)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(R.string.signup_button_label),
                                color = cigar,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = cigar
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Dotted Line
                    DashedDivider(color = cigar.copy(alpha = 0.3f))

                    Spacer(modifier = Modifier.height(20.dp))

                    // Footer Text
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.signup_login_button),
                            color = cigar,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Log In",
                            color = cigar,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            modifier = Modifier.clickable { /* TODO */ }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun DashedDivider(color: Color) {
    Canvas(
        Modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun CreateAccountScreenPreview() {
    CreateAccountScreen()
}
