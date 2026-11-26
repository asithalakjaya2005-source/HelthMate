package com.example.healthmate.data.repository

import com.example.healthmate.data.local.HealthRecordDao
import com.example.healthmate.data.model.HealthRecord
import kotlinx.coroutines.flow.Flow

class HealthRepository(
    private val dao: HealthRecordDao
) {

    fun getAllRecords(
        userId: Int
    ): Flow<List<HealthRecord>> {

        require(userId > 0)

        return dao.getAllRecords(userId)
    }

    suspend fun insert(
        record: HealthRecord,
        userId: Int
    ) {

        require(userId > 0)

        dao.insertRecord(
            record.copy(
                id = 0,
                userId = userId
            )
        )
    }

    suspend fun update(
        record: HealthRecord,
        userId: Int
    ) {

        require(userId > 0)

        dao.updateRecord(
            id = record.id,
            userId = userId,
            date = record.date,
            steps = record.steps,
            calories = record.calories,
            water = record.water
        )
    }

    suspend fun delete(
        record: HealthRecord,
        userId: Int
    ) {

        require(userId > 0)

        dao.deleteRecord(
            id = record.id,
            userId = userId
        )
    }

    fun searchByDate(
        date: String,
        userId: Int
    ): Flow<List<HealthRecord>> {

        require(userId > 0)

        return dao.getRecordsByDate(
            date = date,
            userId = userId
        )
    }

    suspend fun getById(
        id: Int,
        userId: Int
    ): HealthRecord? {

        require(userId > 0)

        return dao.getRecordById(
            id = id,
            userId = userId
        )
    }
}