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
fun RegisterScreen(
    onRegister: (String, String, String) -> Unit,
    onBackToLogin: () -> Unit,
    errorMessage: String = ""
) {

    val colors = MaterialTheme.colorScheme

    var fullName by remember {
        mutableStateOf("")
    }

    var username by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var validationError by remember {
        mutableStateOf("")
    }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = colors.primary,
        focusedLabelColor = colors.primary,
        cursorColor = colors.primary,
        unfocusedBorderColor = colors.outline,
        focusedTextColor = colors.onBackground,
        unfocusedTextColor = colors.onBackground,
        unfocusedLabelColor = colors.onSurfaceVariant
    )

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

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.size(75.dp),
            shape = RoundedCornerShape(22.dp),
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
                    fontSize = 42.sp,
                    color = colors.onPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "HealthMate",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = colors.primary
        )

        Text(
            text = "Your Personal Health Companion",
            fontSize = 14.sp,
            color = colors.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Create Account",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = colors.onBackground,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Start your health journey today",
            fontSize = 14.sp,
            color = colors.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = fullName,
            onValueChange = {
                fullName = it
                validationError = ""
            },
            label = {
                Text("Full Name")
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = textFieldColors
        )

        Spacer(modifier = Modifier.height(12.dp))

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
            colors = textFieldColors
        )

        Spacer(modifier = Modifier.height(12.dp))

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

            colors = textFieldColors
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                validationError = ""
            },
            label = {
                Text("Confirm Password")
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

            colors = textFieldColors
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

                    fullName.isBlank() -> {
                        validationError =
                            "Please enter your full name."
                    }

                    username.isBlank() -> {
                        validationError =
                            "Please enter a username."
                    }

                    username.trim().length < 4 -> {
                        validationError =
                            "Username must contain at least 4 characters."
                    }

                    username.any { it.isWhitespace() } -> {
                        validationError =
                            "Username cannot contain spaces."
                    }

                    password.isBlank() -> {
                        validationError =
                            "Please enter a password."
                    }

                    password.length < 8 -> {
                        validationError =
                            "Password must contain at least 8 characters."
                    }

                    password != confirmPassword -> {
                        validationError =
                            "Passwords do not match."
                    }

                    else -> {

                        validationError = ""

                        onRegister(
                            fullName.trim(),
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
                text = "Create Account",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Already have an account?",
            fontSize = 14.sp,
            color = colors.onSurfaceVariant
        )

        TextButton(
            onClick = onBackToLogin
        ) {
            Text(
                text = "Back to Login",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colors.primary
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

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
fun RegisterScreenPreview() {

    HealthMateTheme(darkTheme = false) {

        RegisterScreen(
            onRegister = { _, _, _ -> },
            onBackToLogin = {}
        )
    }
}

@Preview(
    name = "Dark Mode",
    showBackground = true
)
@Composable
fun RegisterScreenDarkPreview() {

    HealthMateTheme(darkTheme = true) {

        RegisterScreen(
            onRegister = { _, _, _ -> },
            onBackToLogin = {}
        )
    }
}