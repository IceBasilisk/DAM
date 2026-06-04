package a47514.masterplanner.ui

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import a47514.masterplanner.R

@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit = { _, _ -> },
    onSignUpClick: () -> Unit = {},
    onResetPasswordClick: () -> Unit = {}
) {
    val cream = colorResource(R.color.fresh_cream)
    val cigar = colorResource(R.color.cigar)
    val gold = colorResource(R.color.gold)
    val seaBlue = colorResource(R.color.sea_blue)
    val cheesecake = colorResource(R.color.cheesecake)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(cream)
    ) {
        val scrollState = rememberScrollState()

        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Anchor,
                    contentDescription = null,
                    tint = cigar,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = cigar
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = cigar, thickness = 2.dp, modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(modifier = Modifier.height(40.dp))

            // Login Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = cheesecake,
                shadowElevation = 8.dp,
                border = androidx.compose.foundation.BorderStroke(2.dp, cigar)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Boat Icon
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .shadow(4.dp, CircleShape)
                            .background(gold, CircleShape)
                            .border(4.dp, cigar, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsBoat,
                            contentDescription = null,
                            tint = cigar,
                            modifier = Modifier.size(60.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = stringResource(R.string.login_intro),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = cigar,
                        textAlign = TextAlign.Center,
                        lineHeight = 32.sp
                    )

                    Text(
                        text = stringResource(R.string.login_intro_flavor_text),
                        color = cigar.copy(alpha = 0.7f),
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Email Field
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Email",
                            fontWeight = FontWeight.Bold,
                            color = cigar,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(stringResource(R.string.login_email_example), color = Color.Gray) },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = cigar) },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = cream,
                                unfocusedContainerColor = cream,
                                focusedBorderColor = cigar,
                                unfocusedBorderColor = cigar.copy(alpha = 0.3f)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password Field
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Password",
                            fontWeight = FontWeight.Bold,
                            color = cigar,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(stringResource(R.string.login_password_example), color = Color.Gray) },
                            leadingIcon = { Icon(Icons.Default.VpnKey, contentDescription = null, tint = cigar) },
                            shape = RoundedCornerShape(12.dp),
                            visualTransformation = PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = cream,
                                unfocusedContainerColor = cream,
                                focusedBorderColor = cigar,
                                unfocusedBorderColor = cigar.copy(alpha = 0.3f)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.login_reset_password_msg),
                        color = seaBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .align(Alignment.End)
                            .clickable { onResetPasswordClick() }
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Board the Ship Button
                    Button(
                        onClick = { onLoginClick(email, password) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .shadow(4.dp, RoundedCornerShape(16.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = gold),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(4.dp, cigar)
                    ) {
                        Text(
                            text = stringResource(R.string.login_button_label),
                            color = cigar,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Footer
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(
                    text = "New to the archipelago? ",
                    color = cigar
                )
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(2.dp, cigar),
                    color = Color.Transparent,
                    modifier = Modifier.clickable { onSignUpClick() }
                ) {
                    Text(
                        text = stringResource(R.string.login_sign_up_button),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = cigar,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}
