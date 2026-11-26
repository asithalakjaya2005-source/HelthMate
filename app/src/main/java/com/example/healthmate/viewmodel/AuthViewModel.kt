package com.example.healthmate.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthmate.data.local.HealthDatabase
import com.example.healthmate.data.model.User
import com.example.healthmate.data.repository.AuthRepository
import com.example.healthmate.utils.SessionManager
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = AuthRepository(
        HealthDatabase.getDatabase(application).userDao()
    )

    private val sessionManager = SessionManager(application)

    private val _currentUser =
        MutableStateFlow<User?>(null)

    val currentUser = _currentUser.asStateFlow()

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading = _isLoading.asStateFlow()

    private val _isSessionLoading =
        MutableStateFlow(true)

    val isSessionLoading = _isSessionLoading.asStateFlow()

    private val _errorMessage =
        MutableStateFlow<String?>(null)

    val errorMessage = _errorMessage.asStateFlow()

    private val _registrationSuccess =
        MutableStateFlow(false)

    val registrationSuccess =
        _registrationSuccess.asStateFlow()

    init {
        restoreSession()
    }

    private fun restoreSession() {

        viewModelScope.launch {

            try {

                val userId = sessionManager.getUserId()

                if (userId != null) {

                    val user = repository.getUserById(userId)

                    if (user != null) {

                        _currentUser.value = user

                    } else {

                        sessionManager.clearSession()
                        _currentUser.value = null
                    }
                }

            } catch (e: CancellationException) {

                throw e

            } catch (e: Exception) {

                sessionManager.clearSession()
                _currentUser.value = null

            } finally {

                _isSessionLoading.value = false
            }
        }
    }

    fun register(
        fullName: String,
        username: String,
        password: String
    ) {

        if (_isLoading.value || _isSessionLoading.value) return

        _isLoading.value = true
        _errorMessage.value = null
        _registrationSuccess.value = false

        viewModelScope.launch {

            try {

                val result = repository.register(
                    fullName,
                    username,
                    password
                )

                result.onSuccess {

                    _registrationSuccess.value = true

                }.onFailure {

                    _errorMessage.value =
                        it.message ?: "Registration failed."
                }

            } catch (e: CancellationException) {

                throw e

            } catch (e: Exception) {

                _errorMessage.value =
                    "Unable to create account. Please try again."

            } finally {

                _isLoading.value = false
            }
        }
    }

    fun login(
        username: String,
        password: String
    ) {

        if (_isLoading.value || _isSessionLoading.value) return

        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {

            try {

                val result = repository.login(
                    username,
                    password
                )

                result.onSuccess { user ->

                    sessionManager.saveSession(user.id)

                    _currentUser.value = user

                }.onFailure {

                    _errorMessage.value =
                        it.message ?: "Login failed."
                }

            } catch (e: CancellationException) {

                throw e

            } catch (e: Exception) {

                _errorMessage.value =
                    "Unable to login. Please try again."

            } finally {

                _isLoading.value = false
            }
        }
    }

    fun logout() {

        if (_isLoading.value || _isSessionLoading.value) return

        sessionManager.clearSession()

        _currentUser.value = null
        _errorMessage.value = null
        _registrationSuccess.value = false
    }

    fun clearError() {

        _errorMessage.value = null
    }

    fun resetRegistrationSuccess() {

        _registrationSuccess.value = false
    }
}