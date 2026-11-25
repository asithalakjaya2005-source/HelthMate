package com.example.healthmate.ui.records

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthmate.data.model.HealthRecord
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AddHealthRecordScreen(
    onSave: (HealthRecord) -> Unit = {},
    onBack: () -> Unit = {}
) {

    var date by remember {
        mutableStateOf(
            SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).format(Date())
        )
    }

    var steps by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var water by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val primaryColor = Color(0xFF00796B)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F8F7))
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        TextButton(onClick = onBack) {
            Text(
                text = "< Back",
                color = primaryColor
            )
        }

        Text(
            text = "Add Health Record",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor
        )

        Text(
            text = "Enter your daily health information",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date") },
                    placeholder = { Text("YYYY-MM-DD") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = steps,
                    onValueChange = { steps = it },
                    label = { Text("Steps") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = calories,
                    onValueChange = { calories = it },
                    label = { Text("Calories (kcal)") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = water,
                    onValueChange = { water = it },
                    label = { Text("Water Intake (ml)") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                if (errorMessage.isNotEmpty()) {

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {

                        val stepsValue = steps.toIntOrNull()
                        val caloriesValue = calories.toIntOrNull()
                        val waterValue = water.toIntOrNull()

                        val validDate = try {
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

                        if (
                            date.isBlank() ||
                            steps.isBlank() ||
                            calories.isBlank() ||
                            water.isBlank()
                        ) {
                            errorMessage =
                                "Please fill in all fields."

                        } else if (!validDate) {

                            errorMessage =
                                "Enter a valid date (YYYY-MM-DD)."

                        } else if (
                            stepsValue == null ||
                            caloriesValue == null ||
                            waterValue == null ||
                            stepsValue < 0 ||
                            caloriesValue < 0 ||
                            waterValue < 0
                        ) {
                            errorMessage =
                                "Please enter valid positive or zero values."

                        } else {

                            errorMessage = ""

                            val record = HealthRecord(
                                date = date,
                                steps = stepsValue,
                                calories = caloriesValue,
                                water = waterValue
                            )

                            onSave(record)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryColor
                    )
                ) {
                    Text(
                        text = "Save Health Record",
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddHealthRecordPreview() {
    MaterialTheme {
        AddHealthRecordScreen()
    }
}