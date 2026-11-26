package com.example.healthmate.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.healthmate.data.model.HealthRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthRecordDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertRecord(record: HealthRecord)

    @Query("""
        UPDATE health_records
        SET date = :date,
            steps = :steps,
            calories = :calories,
            water = :water
        WHERE id = :id AND userId = :userId
    """)
    suspend fun updateRecord(
        id: Int,
        userId: Int,
        date: String,
        steps: Int,
        calories: Int,
        water: Int
    ): Int

    @Query("""
        DELETE FROM health_records
        WHERE id = :id AND userId = :userId
    """)
    suspend fun deleteRecord(
        id: Int,
        userId: Int
    ): Int

    @Query("""
        SELECT * FROM health_records
        WHERE userId = :userId
        ORDER BY date DESC, id DESC
    """)
    fun getAllRecords(
        userId: Int
    ): Flow<List<HealthRecord>>

    @Query("""
        SELECT * FROM health_records
        WHERE date = :date AND userId = :userId
        ORDER BY id DESC
    """)
    fun getRecordsByDate(
        date: String,
        userId: Int
    ): Flow<List<HealthRecord>>

    @Query("""
        SELECT * FROM health_records
        WHERE id = :id AND userId = :userId
        LIMIT 1
    """)
    suspend fun getRecordById(
        id: Int,
        userId: Int
    ): HealthRecord?
}