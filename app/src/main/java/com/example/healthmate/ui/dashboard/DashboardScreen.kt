package com.example.healthmate.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DashboardScreen(
    totalRecords: Int = 0,
    totalSteps: Int = 0,
    totalCalories: Int = 0,
    totalWater: Int = 0,
    onAddRecord: () -> Unit = {},
    onViewRecords: () -> Unit = {}
) {
    val primaryColor = Color(0xFF00796B)
    val backgroundColor = Color(0xFFF0F5F4)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            text = "HealthMate",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor
        )

        Text(
            text = "Your Personal Health Companion",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = primaryColor
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Welcome to HealthMate!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Track your health and manage your daily health records.",
                    fontSize = 15.sp,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Health Overview",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF263238)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Summary of all saved records",
            fontSize = 13.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HealthSummaryCard(
                title = "Total Steps",
                value = "%,d".format(totalSteps),
                symbol = "👣",
                modifier = Modifier.weight(1f)
            )

            HealthSummaryCard(
                title = "Calories",
                value = "%,d kcal".format(totalCalories),
                symbol = "🔥",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HealthSummaryCard(
                title = "Water Intake",
                value = "%,d ml".format(totalWater),
                symbol = "💧",
                modifier = Modifier.weight(1f)
            )

            HealthSummaryCard(
                title = "Total Records",
                value = totalRecords.toString(),
                symbol = "📋",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Quick Actions",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF263238)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onAddRecord,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryColor
            )
        ) {
            Text(
                text = "+ Add Health Record",
                fontSize = 16.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onViewRecords,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "View Health Records",
                fontSize = 16.sp,
                color = primaryColor
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Your health, your priority.",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Composable
private fun HealthSummaryCard(
    title: String,
    value: String,
    symbol: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.heightIn(min = 140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = symbol,
                fontSize = 26.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = value,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00796B)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    MaterialTheme {
        DashboardScreen(
            totalRecords = 1,
            totalSteps = 8500,
            totalCalories = 500,
            totalWater = 2500
        )
    }
}