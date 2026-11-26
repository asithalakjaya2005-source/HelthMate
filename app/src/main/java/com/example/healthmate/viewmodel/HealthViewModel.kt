package com.example.healthmate.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthmate.data.local.HealthDatabase
import com.example.healthmate.data.model.HealthRecord
import com.example.healthmate.data.repository.HealthRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HealthViewModel(application: Application) :
    AndroidViewModel(application) {

    private val repository: HealthRepository

    private var currentUserId: Int? = null

    private var recordsJob: Job? = null

    private val _allRecords =
        MutableStateFlow<List<HealthRecord>>(emptyList())

    val allRecords: StateFlow<List<HealthRecord>> =
        _allRecords.asStateFlow()

    init {

        val dao = HealthDatabase
            .getDatabase(application)
            .healthRecordDao()

        repository = HealthRepository(dao)
    }

    // Load records for the logged-in user
    fun setUser(userId: Int) {

        require(userId > 0)

        if (currentUserId == userId &&
            recordsJob?.isActive == true
        ) {
            return
        }

        recordsJob?.cancel()

        currentUserId = userId

        _allRecords.value = emptyList()

        recordsJob = viewModelScope.launch {

            repository.getAllRecords(userId)
                .collect { records ->

                    if (currentUserId == userId) {

                        _allRecords.value = records

                    }
                }
        }
    }

    // Add a health record for the current user
    fun addRecord(record: HealthRecord) {

        val userId = currentUserId ?: return

        viewModelScope.launch {

            repository.insert(
                record.copy(
                    id = 0,
                    userId = userId
                ),
                userId
            )
        }
    }

    // Update a record owned by the current user
    fun updateRecord(record: HealthRecord) {

        val userId = currentUserId ?: return

        if (record.userId != userId) return

        viewModelScope.launch {

            repository.update(
                record,
                userId
            )
        }
    }

    // Delete a record owned by the current user
    fun deleteRecord(record: HealthRecord) {

        val userId = currentUserId ?: return

        if (record.userId != userId) return

        viewModelScope.launch {

            repository.delete(
                record,
                userId
            )
        }
    }

    // Clear user data when logging out
    fun clearUser() {

        recordsJob?.cancel()

        recordsJob = null

        currentUserId = null

        _allRecords.value = emptyList()
    }
}