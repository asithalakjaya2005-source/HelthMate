package com.example.healthmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthmate.data.model.HealthRecord
import com.example.healthmate.ui.dashboard.DashboardScreen
import com.example.healthmate.ui.records.AddHealthRecordScreen
import com.example.healthmate.ui.records.EditHealthRecordScreen
import com.example.healthmate.ui.records.HealthRecordsScreen
import com.example.healthmate.ui.theme.HealthMateTheme
import com.example.healthmate.viewmodel.HealthViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            HealthMateTheme {

                val healthViewModel: HealthViewModel =
                    viewModel()

                val records by healthViewModel
                    .allRecords.collectAsState()

                var currentScreen by remember {
                    mutableStateOf("dashboard")
                }

                var selectedRecord by remember {
                    mutableStateOf<HealthRecord?>(null)
                }

                BackHandler(
                    enabled = currentScreen != "dashboard"
                ) {
                    if (currentScreen == "editRecord") {
                        selectedRecord = null
                        currentScreen = "viewRecords"
                    } else {
                        currentScreen = "dashboard"
                    }
                }

                when (currentScreen) {

                    "dashboard" -> {

                        DashboardScreen(
                            totalRecords = records.size,

                            onAddRecord = {
                                currentScreen = "addRecord"
                            },

                            onViewRecords = {
                                currentScreen = "viewRecords"
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

                    "viewRecords" -> {

                        HealthRecordsScreen(

                            records = records,

                            onBack = {
                                currentScreen = "dashboard"
                            },

                            onEdit = { record ->

                                selectedRecord = record

                                currentScreen = "editRecord"
                            },

                            onDelete = { record ->

                                healthViewModel.deleteRecord(record)
                            }
                        )
                    }

                    "editRecord" -> {

                        selectedRecord?.let { record ->

                            EditHealthRecordScreen(

                                record = record,

                                onUpdate = { updatedRecord ->

                                    healthViewModel.updateRecord(
                                        updatedRecord
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