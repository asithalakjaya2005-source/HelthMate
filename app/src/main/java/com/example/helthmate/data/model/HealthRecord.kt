
package com.example.healthmate.data.model

data class HealthRecord(
    val id: Int = 0,
    val date: String,
    val steps: Int,
    val calories: Int,
    val water: Int
)
