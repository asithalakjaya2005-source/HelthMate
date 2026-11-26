package com.example.healthmate.ui.records

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthmate.data.model.HealthRecord
import com.example.healthmate.ui.theme.HealthMateTheme
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun EditHealthRecordScreen(
    record: HealthRecord,
    onUpdate: (HealthRecord) -> Unit,
    onBack: () -> Unit
) {

    val colors = MaterialTheme.colorScheme

    var date by remember(record.id) {
        mutableStateOf(record.date)
    }

    var steps by remember(record.id) {
        mutableStateOf(record.steps.toString())
    }

    var calories by remember(record.id) {
        mutableStateOf(record.calories.toString())
    }

    var water by remember(record.id) {
        mutableStateOf(record.water.toString())
    }

    var errorMessage by remember {
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
            .padding(20.dp)
    ) {

        Text(
            text = "Edit Health Record",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = colors.primary
        )

        Text(
            text = "Update your daily health information",
            fontSize = 14.sp,
            color = colors.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = date,
            onValueChange = {
                date = it
            },
            label = {
                Text("Date (YYYY-MM-DD)")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = textFieldColors
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = steps,
            onValueChange = {
                steps = it
            },
            label = {
                Text("Steps")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = textFieldColors
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = calories,
            onValueChange = {
                calories = it
            },
            label = {
                Text("Calories (kcal)")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = textFieldColors
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = water,
            onValueChange = {
                water = it
            },
            label = {
                Text("Water (ml)")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = textFieldColors
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) {

            Text(
                text = errorMessage,
                color = colors.error,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

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

                    date.matches(
                        Regex("\\d{4}-\\d{2}-\\d{2}")
                    ) && formatter.parse(date) != null

                } catch (e: Exception) {

                    false
                }

                if (!validDate) {

                    errorMessage =
                        "Enter a valid date (YYYY-MM-DD)."

                } else if (
                    stepsValue == null ||
                    caloriesValue == null ||
                    waterValue == null
                ) {

                    errorMessage =
                        "Please enter valid numbers."

                } else if (
                    stepsValue < 0 ||
                    caloriesValue < 0 ||
                    waterValue < 0
                ) {

                    errorMessage =
                        "Values cannot be negative."

                } else {

                    errorMessage = ""

                    val updatedRecord = record.copy(
                        date = date,
                        steps = stepsValue,
                        calories = caloriesValue,
                        water = waterValue
                    )

                    onUpdate(updatedRecord)
                }
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),

            shape = RoundedCornerShape(10.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primary,
                contentColor = colors.onPrimary
            )

        ) {

            Text("Update Record")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onBack,

            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),

            shape = RoundedCornerShape(10.dp),

            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = colors.primary
            )

        ) {

            Text("Cancel")
        }
    }
}

@Preview(
    name = "Light Mode",
    showBackground = true
)
@Composable
fun EditHealthRecordLightPreview() {

    HealthMateTheme(darkTheme = false) {

        EditHealthRecordScreen(
            record = HealthRecord(
                date = "2026-11-26",
                steps = 8500,
                calories = 500,
                water = 2500
            ),
            onUpdate = {},
            onBack = {}
        )
    }
}

@Preview(
    name = "Dark Mode",
    showBackground = true
)
@Composable
fun EditHealthRecordDarkPreview() {

    HealthMateTheme(darkTheme = true) {

        EditHealthRecordScreen(
            record = HealthRecord(
                date = "2026-11-26",
                steps = 8500,
                calories = 500,
                water = 2500
            ),
            onUpdate = {},
            onBack = {}
        )
    }
}