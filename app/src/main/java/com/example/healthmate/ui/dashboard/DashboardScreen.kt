package com.example.healthmate.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthmate.ui.theme.HealthMateTheme

@Composable
fun DashboardScreen(
    totalRecords: Int = 0,
    totalSteps: Int = 0,
    totalCalories: Int = 0,
    totalWater: Int = 0,
    onAddRecord: () -> Unit = {},
    onViewRecords: () -> Unit = {},
    userName: String = "User",
    onViewProgress: () -> Unit = {},
    onLogout: () -> Unit = {},
    darkMode: Boolean = false,
    onThemeToggle: () -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

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
            }

            IconButton(
                onClick = onThemeToggle
            ) {
                Text(
                    text = if (darkMode) "☀️" else "🌙",
                    fontSize = 26.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = colors.primary
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {

                Text(
                    text = "Welcome, $userName!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.onPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Track your health and manage your daily health records.",
                    fontSize = 15.sp,
                    color = colors.onPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Health Overview",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = colors.onBackground
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Summary of all saved records",
            fontSize = 13.sp,
            color = colors.onSurfaceVariant
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
            color = colors.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onAddRecord,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "+ Add Health Record",
                fontSize = 16.sp
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
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onViewProgress,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "View Health Progress",
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "Logout",
                fontSize = 16.sp,
                color = colors.error
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Your health, your priority.",
            fontSize = 14.sp,
            color = colors.onSurfaceVariant
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
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = modifier.heightIn(min = 140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface
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
                color = colors.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = value,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = colors.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardLightPreview() {
    HealthMateTheme(darkTheme = false) {
        DashboardScreen(
            userName = "User",
            totalRecords = 1,
            totalSteps = 8500,
            totalCalories = 500,
            totalWater = 2500,
            darkMode = false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardDarkPreview() {
    HealthMateTheme(darkTheme = true) {
        DashboardScreen(
            userName = "User",
            totalRecords = 1,
            totalSteps = 8500,
            totalCalories = 500,
            totalWater = 2500,
            darkMode = true
        )
    }
}