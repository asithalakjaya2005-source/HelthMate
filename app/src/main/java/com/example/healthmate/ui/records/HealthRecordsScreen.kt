package com.example.healthmate.ui.records

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthmate.data.model.HealthRecord

@Composable
fun HealthRecordsScreen(
    records: List<HealthRecord>,
    onBack: () -> Unit,
    onEdit: (HealthRecord) -> Unit,
    onDelete: (HealthRecord) -> Unit
) {
    val primaryColor = Color(0xFF00796B)

    var recordToDelete by remember {
        mutableStateOf<HealthRecord?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F6F5))
            .safeDrawingPadding()
            .padding(20.dp)
    ) {

        Text(
            text = "Health Records",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor
        )

        Text(
            text = "View your daily health information",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Total Records: ${records.size}",
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (records.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "No health records available.",
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                items(
                    items = records,
                    key = { it.id }
                ) { record ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation =
                            CardDefaults.cardElevation(
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
                                color = primaryColor
                            )

                            Spacer(
                                modifier = Modifier.height(12.dp)
                            )

                            Text("Steps: ${record.steps}")

                            Spacer(
                                modifier = Modifier.height(6.dp)
                            )

                            Text(
                                "Calories: ${record.calories} kcal"
                            )

                            Spacer(
                                modifier = Modifier.height(6.dp)
                            )

                            Text("Water: ${record.water} ml")

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
                                            contentColor = primaryColor
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
                                            contentColor = Color.Red
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
                containerColor = primaryColor
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
                        color = Color.Red
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        recordToDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}