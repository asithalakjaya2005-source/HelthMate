package com.example.healthmate

import android.os.Bundle
import android.graphics.Color as AndroidColor
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthmate.data.model.HealthRecord
import com.example.healthmate.ui.auth.LoginScreen
import com.example.healthmate.ui.auth.RegisterScreen
import com.example.healthmate.ui.dashboard.DashboardScreen
import com.example.healthmate.ui.records.AddHealthRecordScreen
import com.example.healthmate.ui.records.EditHealthRecordScreen
import com.example.healthmate.ui.records.HealthRecordsScreen
import com.example.healthmate.ui.theme.HealthMateTheme
import com.example.healthmate.ui.progress.HealthProgressScreen
import com.example.healthmate.viewmodel.HealthViewModel
import com.example.healthmate.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val themePreferences = getSharedPreferences(
            "healthmate_settings",
            MODE_PRIVATE
        )

        setContent {

            val systemDarkTheme = isSystemInDarkTheme()

            var darkMode by remember {
                mutableStateOf(
                    themePreferences.getBoolean(
                        "dark_mode",
                        systemDarkTheme
                    )
                )
            }

            LaunchedEffect(darkMode) {

                if (darkMode) {

                    enableEdgeToEdge(
                        statusBarStyle = SystemBarStyle.dark(
                            AndroidColor.TRANSPARENT
                        ),
                        navigationBarStyle = SystemBarStyle.dark(
                            AndroidColor.TRANSPARENT
                        )
                    )

                } else {

                    enableEdgeToEdge(
                        statusBarStyle = SystemBarStyle.light(
                            AndroidColor.TRANSPARENT,
                            AndroidColor.BLACK
                        ),
                        navigationBarStyle = SystemBarStyle.light(
                            AndroidColor.TRANSPARENT,
                            AndroidColor.BLACK
                        )
                    )
                }
            }

            val onThemeToggle: () -> Unit = {

                darkMode = !darkMode

                themePreferences.edit()
                    .putBoolean("dark_mode", darkMode)
                    .apply()
            }

            HealthMateTheme(darkTheme = darkMode) {

                val authViewModel: AuthViewModel =
                    viewModel()

                val healthViewModel: HealthViewModel =
                    viewModel()

                val currentUser by authViewModel
                    .currentUser.collectAsState()

                val isSessionLoading by authViewModel
                    .isSessionLoading.collectAsState()

                val errorMessage by authViewModel
                    .errorMessage.collectAsState()

                val registrationSuccess by authViewModel
                    .registrationSuccess.collectAsState()

                val allRecords by healthViewModel
                    .allRecords.collectAsState()

                val userId = currentUser?.id

                val records = allRecords.filter {
                    userId != null && it.userId == userId
                }

                var currentScreen by remember {
                    mutableStateOf("login")
                }

                var selectedRecord by remember {
                    mutableStateOf<HealthRecord?>(null)
                }

                // Restore authenticated user session
                LaunchedEffect(userId, isSessionLoading) {

                    if (!isSessionLoading) {

                        selectedRecord = null

                        if (userId != null) {

                            healthViewModel.setUser(userId)

                            currentScreen = "dashboard"

                        } else {

                            healthViewModel.clearUser()

                            currentScreen = "login"
                        }
                    }
                }

                // Return to login screen after registration
                LaunchedEffect(registrationSuccess) {

                    if (registrationSuccess) {

                        authViewModel.clearError()

                        currentScreen = "login"

                        authViewModel.resetRegistrationSuccess()
                    }
                }

                // Handle Android back navigation
                BackHandler(
                    enabled = !isSessionLoading &&
                            currentScreen != "dashboard" &&
                            currentScreen != "login"
                ) {

                    when (currentScreen) {

                        "register" -> {

                            authViewModel.clearError()

                            currentScreen = "login"
                        }

                        "editRecord" -> {

                            selectedRecord = null

                            currentScreen = "viewRecords"
                        }

                        else -> {

                            currentScreen =
                                if (currentUser != null) {
                                    "dashboard"
                                } else {
                                    "login"
                                }
                        }
                    }
                }

                // Display loading screen while restoring session
                if (isSessionLoading) {

                    SessionLoadingScreen()

                } else if (currentUser == null) {

                    // Authentication screens
                    when (currentScreen) {

                        "register" -> {

                            RegisterScreen(

                                onRegister = {
                                        fullName,
                                        username,
                                        password ->

                                    authViewModel.register(
                                        fullName,
                                        username,
                                        password
                                    )
                                },

                                onBackToLogin = {

                                    authViewModel.clearError()

                                    currentScreen = "login"
                                },

                                errorMessage =
                                    errorMessage ?: ""
                            )
                        }

                        else -> {

                            LoginScreen(

                                onLogin = {
                                        username, password ->

                                    authViewModel.login(
                                        username,
                                        password
                                    )
                                },

                                onCreateAccount = {

                                    authViewModel.clearError()

                                    currentScreen = "register"
                                },

                                errorMessage =
                                    errorMessage ?: ""
                            )
                        }
                    }

                } else {

                    // Authenticated user screens
                    when (currentScreen) {

                        "dashboard", "login", "register" -> {

                            DashboardScreen(

                                darkMode = darkMode,

                                onThemeToggle = onThemeToggle,

                                userName =
                                    currentUser?.fullName ?: "User",

                                totalRecords = records.size,

                                totalSteps = records.sumOf {
                                    it.steps
                                },

                                totalCalories = records.sumOf {
                                    it.calories
                                },

                                totalWater = records.sumOf {
                                    it.water
                                },

                                onAddRecord = {

                                    currentScreen = "addRecord"
                                },

                                onViewRecords = {

                                    currentScreen = "viewRecords"
                                },

                                onViewProgress = {

                                    currentScreen = "progress"
                                },

                                onLogout = {

                                    authViewModel.logout()

                                    if (
                                        authViewModel.currentUser.value == null
                                    ) {

                                        selectedRecord = null

                                        healthViewModel.clearUser()

                                        currentScreen = "login"
                                    }
                                }
                            )
                        }

                        "progress" -> {

                            HealthProgressScreen(

                                records = records,

                                onBack = {

                                    currentScreen = "dashboard"
                                }
                            )
                        }

                        "addRecord" -> {

                            AddHealthRecordScreen(

                                onSave = { record ->

                                    healthViewModel.addRecord(
                                        record
                                    )

                                    currentScreen = "dashboard"
                                },

                                onBack = {

                                    currentScreen = "dashboard"
                                }
                            )
                        }

                        "viewRecords" -> {

                            HealthRecordsScreen(

                                records = records,

                                onBack = {

                                    currentScreen = "dashboard"
                                },

                                onEdit = { record ->

                                    if (record.userId == userId) {

                                        selectedRecord = record

                                        currentScreen = "editRecord"
                                    }
                                },

                                onDelete = { record ->

                                    if (record.userId == userId) {

                                        healthViewModel.deleteRecord(
                                            record
                                        )
                                    }
                                }
                            )
                        }

                        "editRecord" -> {

                            selectedRecord?.let { record ->

                                if (record.userId == userId) {

                                    EditHealthRecordScreen(

                                        record = record,

                                        onUpdate = { updatedRecord ->

                                            healthViewModel.updateRecord(
                                                updatedRecord.copy(
                                                    id = record.id,
                                                    userId = record.userId
                                                )
                                            )

                                            selectedRecord = null

                                            currentScreen = "viewRecords"
                                        },

                                        onBack = {

                                            selectedRecord = null

                                            currentScreen = "viewRecords"
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SessionLoadingScreen() {

    val colors = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .safeDrawingPadding(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "HealthMate",
                style = MaterialTheme.typography.headlineMedium,
                color = colors.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            CircularProgressIndicator(
                color = colors.primary,
                modifier = Modifier.size(42.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Loading...",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onBackground
            )
        }
    }
}