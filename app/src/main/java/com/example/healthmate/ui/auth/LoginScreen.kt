package com.example.healthmate.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthmate.ui.theme.HealthMateTheme

@Composable
fun LoginScreen(
    onLogin: (String, String) -> Unit,
    onCreateAccount: () -> Unit,
    errorMessage: String = ""
) {

    val colors = MaterialTheme.colorScheme

    var username by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var validationError by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .safeDrawingPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(48.dp))

        Card(
            modifier = Modifier.size(85.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = colors.primary
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "♥",
                    fontSize = 48.sp,
                    color = colors.onPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "HealthMate",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = colors.primary
        )

        Text(
            text = "Your Personal Health Companion",
            fontSize = 14.sp,
            color = colors.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Welcome Back!",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = colors.onBackground,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Login to your account",
            fontSize = 15.sp,
            color = colors.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                validationError = ""
            },
            label = {
                Text("Username")
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colors.primary,
                focusedLabelColor = colors.primary,
                cursorColor = colors.primary,
                unfocusedBorderColor = colors.outline,
                focusedTextColor = colors.onBackground,
                unfocusedTextColor = colors.onBackground,
                unfocusedLabelColor = colors.onSurfaceVariant
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                validationError = ""
            },
            label = {
                Text("Password")
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),

            visualTransformation =
                if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },

            trailingIcon = {
                TextButton(
                    onClick = {
                        passwordVisible = !passwordVisible
                    }
                ) {
                    Text(
                        text = if (passwordVisible) {
                            "Hide"
                        } else {
                            "Show"
                        },
                        color = colors.primary
                    )
                }
            },

            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colors.primary,
                focusedLabelColor = colors.primary,
                cursorColor = colors.primary,
                unfocusedBorderColor = colors.outline,
                focusedTextColor = colors.onBackground,
                unfocusedTextColor = colors.onBackground,
                unfocusedLabelColor = colors.onSurfaceVariant
            )
        )

        val displayedError =
            validationError.ifEmpty { errorMessage }

        if (displayedError.isNotEmpty()) {

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = displayedError,
                color = colors.error,
                fontSize = 13.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                when {
                    username.isBlank() -> {
                        validationError =
                            "Please enter your username."
                    }

                    password.isBlank() -> {
                        validationError =
                            "Please enter your password."
                    }

                    else -> {
                        validationError = ""

                        onLogin(
                            username.trim(),
                            password
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primary,
                contentColor = colors.onPrimary
            )
        ) {
            Text(
                text = "Login",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Don't have an account?",
            fontSize = 14.sp,
            color = colors.onSurfaceVariant
        )

        TextButton(
            onClick = onCreateAccount
        ) {
            Text(
                text = "Create Account",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colors.primary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Your health, your priority.",
            fontSize = 13.sp,
            color = colors.onSurfaceVariant
        )
    }
}

@Preview(
    name = "Light Mode",
    showBackground = true
)
@Composable
fun LoginScreenPreview() {

    HealthMateTheme(darkTheme = false) {

        LoginScreen(
            onLogin = { _, _ -> },
            onCreateAccount = {}
        )
    }
}

@Preview(
    name = "Dark Mode",
    showBackground = true
)
@Composable
fun LoginScreenDarkPreview() {

    HealthMateTheme(darkTheme = true) {

        LoginScreen(
            onLogin = { _, _ -> },
            onCreateAccount = {}
        )
    }
}