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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthmate.data.model.HealthRecord

@Composable
fun HealthProgressScreen(
    records: List<HealthRecord>,
    onBack: () -> Unit
) {
    val primaryColor = Color(0xFF00796B)
    val backgroundColor = Color(0xFFEFF5F4)

    val sortedRecords = records
        .sortedBy { it.date }
        .takeLast(7)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .systemBarsPadding()
            .padding(20.dp)
    ) {

        Text(
            text = "Health Progress",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor
        )

        Text(
            text = "Track your health activity over time",
            fontSize = 14.sp,
            color = Color.Gray
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
                    color = Color.Gray,
                    fontSize = 16.sp
                )

            } else {

                Text(
                    text = "Latest 7 Health Records",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                ProgressChartCard(
                    title = "👣 Steps Progress",
                    values = sortedRecords.map { it.steps },
                    labels = sortedRecords.map {
                        it.date.takeLast(5)
                    },
                    chartColor = primaryColor,
                    unit = "steps"
                )

                ProgressChartCard(
                    title = "🔥 Calories Progress",
                    values = sortedRecords.map { it.calories },
                    labels = sortedRecords.map {
                        it.date.takeLast(5)
                    },
                    chartColor = Color(0xFFF57C00),
                    unit = "kcal",
                    lineChart = true
                )

                ProgressChartCard(
                    title = "💧 Water Intake Progress",
                    values = sortedRecords.map { it.water },
                    labels = sortedRecords.map {
                        it.date.takeLast(5)
                    },
                    chartColor = Color(0xFF2196F3),
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
                containerColor = primaryColor
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

    val maxValue = (values.maxOrNull() ?: 1)
        .coerceAtLeast(1)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
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
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(20.dp))

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {

                if (values.isNotEmpty()) {

                    val slotWidth = size.width / values.size
                    val barWidth = slotWidth * 0.55f
                    val chartHeight = size.height - 12.dp.toPx()

                    // Draw horizontal grid lines
                    for (i in 0..4) {

                        val y = size.height -
                                (chartHeight * i / 4f)

                        drawLine(
                            color = Color(0xFFE0E0E0),
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = 1.dp.toPx()
                        )
                    }

                    if (lineChart) {

                        val points = values.mapIndexed { index, value ->

                            val x = slotWidth * index +
                                    slotWidth / 2

                            val y = size.height -
                                    (value.coerceAtLeast(0).toFloat() /
                                            maxValue) * chartHeight

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

                        values.forEachIndexed { index, value ->

                            val barHeight =
                                (value.coerceAtLeast(0).toFloat() /
                                        maxValue) * chartHeight

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
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}