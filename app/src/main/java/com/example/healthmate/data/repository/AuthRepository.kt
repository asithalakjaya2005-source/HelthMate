package com.example.healthmate.data.repository

import android.database.sqlite.SQLiteConstraintException
import com.example.healthmate.data.local.UserDao
import com.example.healthmate.data.model.User
import com.example.healthmate.utils.PasswordUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

class AuthRepository(
    private val userDao: UserDao
) {

    suspend fun register(
        fullName: String,
        username: String,
        password: String
    ): Result<User> = withContext(Dispatchers.IO) {

        val cleanName = fullName.trim()
        val cleanUsername = username.trim()
            .lowercase(Locale.ROOT)

        if (cleanName.isBlank()) {
            return@withContext Result.failure(
                IllegalArgumentException(
                    "Please enter your full name."
                )
            )
        }

        if (cleanUsername.length < 4) {
            return@withContext Result.failure(
                IllegalArgumentException(
                    "Username must contain at least 4 characters."
                )
            )
        }

        if (password.length < 8) {
            return@withContext Result.failure(
                IllegalArgumentException(
                    "Password must contain at least 8 characters."
                )
            )
        }

        if (userDao.isUsernameExists(cleanUsername)) {
            return@withContext Result.failure(
                IllegalArgumentException(
                    "Username already exists."
                )
            )
        }

        val salt = PasswordUtils.generateSalt()

        val passwordHash = PasswordUtils.hashPassword(
            password,
            salt
        )

        val user = User(
            fullName = cleanName,
            username = cleanUsername,
            passwordHash = passwordHash,
            passwordSalt = salt
        )

        try {

            val userId = userDao.registerUser(user)

            Result.success(
                user.copy(id = userId.toInt())
            )

        } catch (e: SQLiteConstraintException) {

            Result.failure(
                IllegalArgumentException(
                    "Username already exists."
                )
            )
        }
    }

    suspend fun login(
        username: String,
        password: String
    ): Result<User> = withContext(Dispatchers.IO) {

        val cleanUsername = username.trim()
            .lowercase(Locale.ROOT)

        if (cleanUsername.isBlank() || password.isBlank()) {
            return@withContext Result.failure(
                IllegalArgumentException(
                    "Please enter username and password."
                )
            )
        }

        val user = userDao.getUserByUsername(
            cleanUsername
        )

        if (user == null) {
            return@withContext Result.failure(
                IllegalArgumentException(
                    "Invalid username or password."
                )
            )
        }

        val isPasswordCorrect =
            PasswordUtils.verifyPassword(
                password = password,
                salt = user.passwordSalt,
                storedHash = user.passwordHash
            )

        if (isPasswordCorrect) {

            Result.success(user)

        } else {

            Result.failure(
                IllegalArgumentException(
                    "Invalid username or password."
                )
            )
        }
    }

    suspend fun getUserById(
        userId: Int
    ): User? = withContext(Dispatchers.IO) {

        userDao.getUserById(userId)
    }
}