package com.example.healthmate.ui.records

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthmate.data.model.HealthRecord
import com.example.healthmate.ui.theme.HealthMateTheme
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HealthRecordsScreen(
    records: List<HealthRecord>,
    onBack: () -> Unit,
    onEdit: (HealthRecord) -> Unit,
    onDelete: (HealthRecord) -> Unit
) {

    val colors = MaterialTheme.colorScheme

    var searchDate by remember {
        mutableStateOf("")
    }

    var appliedDate by remember {
        mutableStateOf("")
    }

    var searchError by remember {
        mutableStateOf("")
    }

    var recordToDelete by remember {
        mutableStateOf<HealthRecord?>(null)
    }

    val filteredRecords = remember(records, appliedDate) {
        if (appliedDate.isBlank()) {
            records
        } else {
            records.filter {
                it.date == appliedDate
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .safeDrawingPadding()
            .imePadding()
            .padding(20.dp)
    ) {

        Text(
            text = "Health Records",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = colors.primary
        )

        Text(
            text = "View your daily health information",
            fontSize = 14.sp,
            color = colors.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = searchDate,
            onValueChange = {
                searchDate = it
                searchError = ""
            },
            label = {
                Text("Search by Date")
            },
            placeholder = {
                Text("YYYY-MM-DD")
            },
            singleLine = true,
            isError = searchError.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        if (searchError.isNotEmpty()) {
            Text(
                text = searchError,
                color = colors.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Button(
                onClick = {
                    val date = searchDate.trim()

                    if (isValidSearchDate(date)) {
                        appliedDate = date
                        searchError = ""
                    } else {
                        searchError =
                            "Enter a valid date (YYYY-MM-DD)"
                    }
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary
                )
            ) {
                Text("Search")
            }

            OutlinedButton(
                onClick = {
                    searchDate = ""
                    appliedDate = ""
                    searchError = ""
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = colors.primary
                )
            ) {
                Text("Clear Filter")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (appliedDate.isBlank()) {
                "Total Records: ${records.size}"
            } else {
                "Matching Records: ${filteredRecords.size}"
            },
            fontWeight = FontWeight.Medium,
            color = colors.onBackground
        )

        if (appliedDate.isNotBlank()) {
            Text(
                text = "Selected Date: $appliedDate",
                fontSize = 13.sp,
                color = colors.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredRecords.isEmpty()) {

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = if (appliedDate.isBlank()) {
                        "No health records available."
                    } else {
                        "No health records found for $appliedDate."
                    },
                    color = colors.onSurfaceVariant
                )
            }

        } else {

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                items(
                    items = filteredRecords,
                    key = { it.id }
                ) { record ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),

                        colors = CardDefaults.cardColors(
                            containerColor = colors.surface,
                            contentColor = colors.onSurface
                        ),

                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = "Date: ${record.date}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.primary
                            )

                            Spacer(
                                modifier = Modifier.height(12.dp)
                            )

                            Text(
                                text = "Steps: ${record.steps}",
                                color = colors.onSurface
                            )

                            Spacer(
                                modifier = Modifier.height(6.dp)
                            )

                            Text(
                                text = "Calories: ${record.calories} kcal",
                                color = colors.onSurface
                            )

                            Spacer(
                                modifier = Modifier.height(6.dp)
                            )

                            Text(
                                text = "Water: ${record.water} ml",
                                color = colors.onSurface
                            )

                            Spacer(
                                modifier = Modifier.height(16.dp)
                            )

                            Row(
                                horizontalArrangement =
                                    Arrangement.spacedBy(10.dp)
                            ) {

                                OutlinedButton(
                                    onClick = {
                                        onEdit(record)
                                    },

                                    modifier = Modifier.weight(1f),

                                    colors =
                                        ButtonDefaults.outlinedButtonColors(
                                            contentColor = colors.primary
                                        )
                                ) {
                                    Text("Edit")
                                }

                                OutlinedButton(
                                    onClick = {
                                        recordToDelete = record
                                    },

                                    modifier = Modifier.weight(1f),

                                    colors =
                                        ButtonDefaults.outlinedButtonColors(
                                            contentColor = colors.error
                                        )
                                ) {
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onBack,

            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),

            shape = RoundedCornerShape(10.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primary,
                contentColor = colors.onPrimary
            )
        ) {
            Text("Back to Dashboard")
        }
    }

    recordToDelete?.let { record ->

        AlertDialog(
            onDismissRequest = {
                recordToDelete = null
            },

            containerColor = colors.surface,
            titleContentColor = colors.onSurface,
            textContentColor = colors.onSurfaceVariant,

            title = {
                Text("Delete Health Record")
            },

            text = {
                Text(
                    "Are you sure you want to delete this health record?"
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {
                        onDelete(record)
                        recordToDelete = null
                    }
                ) {
                    Text(
                        text = "Delete",
                        color = colors.error
                    )
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        recordToDelete = null
                    }
                ) {
                    Text(
                        text = "Cancel",
                        color = colors.primary
                    )
                }
            }
        )
    }
}

private fun isValidSearchDate(date: String): Boolean {

    if (!date.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
        return false
    }

    return try {

        val formatter = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.US
        )

        formatter.isLenient = false

        val parsedDate = formatter.parse(date)

        parsedDate != null &&
                formatter.format(parsedDate) == date

    } catch (e: Exception) {
        false
    }
}

@Preview(
    name = "Light Mode",
    showBackground = true
)
@Composable
fun HealthRecordsLightPreview() {

    HealthMateTheme(darkTheme = false) {

        HealthRecordsScreen(
            records = listOf(
                HealthRecord(
                    date = "2026-11-26",
                    steps = 8500,
                    calories = 500,
                    water = 2500
                )
            ),
            onBack = {},
            onEdit = {},
            onDelete = {}
        )
    }
}

@Preview(
    name = "Dark Mode",
    showBackground = true
)
@Composable
fun HealthRecordsDarkPreview() {

    HealthMateTheme(darkTheme = true) {

        HealthRecordsScreen(
            records = listOf(
                HealthRecord(
                    date = "2026-11-26",
                    steps = 8500,
                    calories = 500,
                    water = 2500
                )
            ),
            onBack = {},
            onEdit = {},
            onDelete = {}
        )
    }
}