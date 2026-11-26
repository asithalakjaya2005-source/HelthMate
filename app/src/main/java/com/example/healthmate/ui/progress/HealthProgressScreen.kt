package com.example.healthmate.ui.progress

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthmate.data.model.HealthRecord
import com.example.healthmate.ui.theme.HealthMateTheme

@Composable
fun HealthProgressScreen(
    records: List<HealthRecord>,
    onBack: () -> Unit
) {

    val colors = MaterialTheme.colorScheme

    val isDarkTheme = colors.background.luminance() < 0.5f

    val stepsColor = colors.primary

    val caloriesColor = if (isDarkTheme) {
        Color(0xFFFFB74D)
    } else {
        Color(0xFFC65D00)
    }

    val waterColor = if (isDarkTheme) {
        Color(0xFF64B5F6)
    } else {
        Color(0xFF1565C0)
    }

    val sortedRecords = records
        .sortedBy { it.date }
        .takeLast(7)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .safeDrawingPadding()
            .padding(20.dp)
    ) {

        Text(
            text = "Health Progress",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = colors.primary
        )

        Text(
            text = "Track your health activity over time",
            fontSize = 14.sp,
            color = colors.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            if (sortedRecords.isEmpty()) {

                Text(
                    text = "No health records available.",
                    color = colors.onSurfaceVariant,
                    fontSize = 16.sp
                )

            } else {

                Text(
                    text = "Latest 7 Health Records",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.onBackground
                )

                ProgressChartCard(
                    title = "👣 Steps Progress",
                    values = sortedRecords.map {
                        it.steps
                    },
                    labels = sortedRecords.map {
                        it.date.takeLast(5)
                    },
                    chartColor = stepsColor,
                    unit = "steps"
                )

                ProgressChartCard(
                    title = "🔥 Calories Progress",
                    values = sortedRecords.map {
                        it.calories
                    },
                    labels = sortedRecords.map {
                        it.date.takeLast(5)
                    },
                    chartColor = caloriesColor,
                    unit = "kcal",
                    lineChart = true
                )

                ProgressChartCard(
                    title = "💧 Water Intake Progress",
                    values = sortedRecords.map {
                        it.water
                    },
                    labels = sortedRecords.map {
                        it.date.takeLast(5)
                    },
                    chartColor = waterColor,
                    unit = "ml"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onBack,

            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),

            shape = RoundedCornerShape(10.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primary,
                contentColor = colors.onPrimary
            )

        ) {

            Text("Back to Dashboard")
        }
    }
}

@Composable
private fun ProgressChartCard(
    title: String,
    values: List<Int>,
    labels: List<String>,
    chartColor: Color,
    unit: String,
    lineChart: Boolean = false
) {

    val colors = MaterialTheme.colorScheme

    val gridColor = colors.outline.copy(
        alpha = 0.35f
    )

    val maxValue = (values.maxOrNull() ?: 1)
        .coerceAtLeast(1)

    Card(
        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(16.dp),

        colors = CardDefaults.cardColors(
            containerColor = colors.surface,
            contentColor = colors.onSurface
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = chartColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Highest: ${values.maxOrNull() ?: 0} $unit",
                fontSize = 13.sp,
                color = colors.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {

                if (values.isNotEmpty()) {

                    val slotWidth =
                        size.width / values.size

                    val barWidth =
                        slotWidth * 0.55f

                    val chartHeight =
                        size.height - 12.dp.toPx()

                    // Draw horizontal grid lines

                    for (i in 0..4) {

                        val y = size.height -
                                (chartHeight * i / 4f)

                        drawLine(
                            color = gridColor,
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = 1.dp.toPx()
                        )
                    }

                    if (lineChart) {

                        val points = values.mapIndexed {
                                index, value ->

                            val x = slotWidth * index +
                                    slotWidth / 2

                            val y = size.height -
                                    (
                                            value.coerceAtLeast(0)
                                                .toFloat() / maxValue
                                            ) * chartHeight

                            Offset(x, y)
                        }

                        for (i in 0 until points.lastIndex) {

                            drawLine(
                                color = chartColor,
                                start = points[i],
                                end = points[i + 1],
                                strokeWidth = 3.dp.toPx()
                            )
                        }

                        points.forEach { point ->

                            drawCircle(
                                color = chartColor,
                                radius = 4.dp.toPx(),
                                center = point
                            )
                        }

                    } else {

                        values.forEachIndexed {
                                index, value ->

                            val barHeight =
                                (
                                        value.coerceAtLeast(0)
                                            .toFloat() / maxValue
                                        ) * chartHeight

                            val x = slotWidth * index +
                                    (slotWidth - barWidth) / 2

                            drawRoundRect(
                                color = chartColor,

                                topLeft = Offset(
                                    x,
                                    size.height - barHeight
                                ),

                                size = Size(
                                    barWidth,
                                    barHeight
                                ),

                                cornerRadius = CornerRadius(
                                    5.dp.toPx()
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                labels.forEach { label ->

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = label,
                            fontSize = 10.sp,
                            color = colors.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

private fun sampleProgressRecords(): List<HealthRecord> {

    return listOf(

        HealthRecord(
            date = "2026-11-20",
            steps = 4500,
            calories = 300,
            water = 1500
        ),

        HealthRecord(
            date = "2026-11-21",
            steps = 6000,
            calories = 450,
            water = 2000
        ),

        HealthRecord(
            date = "2026-11-22",
            steps = 5500,
            calories = 350,
            water = 1800
        ),

        HealthRecord(
            date = "2026-11-23",
            steps = 8000,
            calories = 550,
            water = 2500
        ),

        HealthRecord(
            date = "2026-11-24",
            steps = 7000,
            calories = 400,
            water = 2200
        ),

        HealthRecord(
            date = "2026-11-25",
            steps = 9000,
            calories = 650,
            water = 2800
        ),

        HealthRecord(
            date = "2026-11-26",
            steps = 8500,
            calories = 500,
            water = 2500
        )
    )
}

@Preview(
    name = "Light Mode",
    showBackground = true
)
@Composable
fun HealthProgressLightPreview() {

    HealthMateTheme(darkTheme = false) {

        HealthProgressScreen(
            records = sampleProgressRecords(),
            onBack = {}
        )
    }
}

@Preview(
    name = "Dark Mode",
    showBackground = true
)
@Composable
fun HealthProgressDarkPreview() {

    HealthMateTheme(darkTheme = true) {

        HealthProgressScreen(
            records = sampleProgressRecords(),
            onBack = {}
        )
    }
}