
package com.example.healthmate.data.repository

import com.example.healthmate.data.local.HealthRecordDao
import com.example.healthmate.data.model.HealthRecord
import kotlinx.coroutines.flow.Flow

class HealthRepository(
    private val dao: HealthRecordDao
) {

    val allRecords: Flow<List<HealthRecord>> =
        dao.getAllRecords()

    suspend fun insert(record: HealthRecord) {
        dao.insertRecord(record)
    }

    suspend fun update(record: HealthRecord) {
        dao.updateRecord(record)
    }

    suspend fun delete(record: HealthRecord) {
        dao.deleteRecord(record)
    }

    fun searchByDate(date: String): Flow<List<HealthRecord>> {
        return dao.getRecordsByDate(date)
    }

    suspend fun getById(id: Int): HealthRecord? {
        return dao.getRecordById(id)
    }
}
