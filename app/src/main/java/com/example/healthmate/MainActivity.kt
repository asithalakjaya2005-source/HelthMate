package com.example.healthmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthmate.ui.dashboard.DashboardScreen
import com.example.healthmate.ui.records.AddHealthRecordScreen
import com.example.healthmate.ui.theme.HealthMateTheme
import com.example.healthmate.viewmodel.HealthViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            HealthMateTheme {

                val healthViewModel: HealthViewModel = viewModel()

                val records by healthViewModel.allRecords.collectAsState()

                var currentScreen by rememberSaveable {
                    mutableStateOf("dashboard")
                }

                when (currentScreen) {

                    "dashboard" -> {
                        DashboardScreen(
                            totalRecords = records.size,
                            onAddRecord = {
                                currentScreen = "addRecord"
                            },
                            onViewRecords = {
                                // Records screen will be added later
                            }
                        )
                    }

                    "addRecord" -> {
                        AddHealthRecordScreen(
                            onSave = { record ->
                                healthViewModel.addRecord(record)
                                currentScreen = "dashboard"
                            },
                            onBack = {
                                currentScreen = "dashboard"
                            }
                        )
                    }
                }
            }
        }
    }
}