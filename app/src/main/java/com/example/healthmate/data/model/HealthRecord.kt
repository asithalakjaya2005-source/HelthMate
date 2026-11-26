package com.example.healthmate.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "health_records")
data class HealthRecord(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val date: String,

    val steps: Int,

    val calories: Int,

    val water: Int,

    @ColumnInfo(defaultValue = "0")
    val userId: Int = 0
)