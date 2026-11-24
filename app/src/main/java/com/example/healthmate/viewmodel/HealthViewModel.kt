package com.example.healthmate.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthmate.data.local.HealthDatabase
import com.example.healthmate.data.model.HealthRecord
import com.example.healthmate.data.repository.HealthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HealthViewModel(application: Application) :
    AndroidViewModel(application) {

    private val repository: HealthRepository

    val allRecords: StateFlow<List<HealthRecord>>

    init {
        val dao = HealthDatabase
            .getDatabase(application)
            .healthRecordDao()

        repository = HealthRepository(dao)

        allRecords = repository.allRecords.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun addRecord(record: HealthRecord) {
        viewModelScope.launch {
            repository.insert(record)
        }
    }

    fun updateRecord(record: HealthRecord) {
        viewModelScope.launch {
            repository.update(record)
        }
    }

    fun deleteRecord(record: HealthRecord) {
        viewModelScope.launch {
            repository.delete(record)
        }
    }
}
