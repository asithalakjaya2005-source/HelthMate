package com.example.healthmate.utils

import android.util.Base64
import java.security.SecureRandom
import java.security.MessageDigest
import org.bouncycastle.crypto.PBEParametersGenerator
import org.bouncycastle.crypto.digests.SHA256Digest
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator
import org.bouncycastle.crypto.params.KeyParameter

object PasswordUtils {

    private const val ITERATIONS = 600000
    private const val KEY_LENGTH = 256
    private const val SALT_LENGTH = 16

    fun generateSalt(): String {

        val salt = ByteArray(SALT_LENGTH)

        SecureRandom().nextBytes(salt)

        return Base64.encodeToString(
            salt,
            Base64.NO_WRAP
        )
    }

    fun hashPassword(
        password: String,
        salt: String
    ): String {

        val saltBytes = Base64.decode(
            salt,
            Base64.NO_WRAP
        )

        val passwordChars = password.toCharArray()

        val passwordBytes =
            PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(
                passwordChars
            )

        return try {

            val generator = PKCS5S2ParametersGenerator(
                SHA256Digest()
            )

            generator.init(
                passwordBytes,
                saltBytes,
                ITERATIONS
            )

            val keyParameter =
                generator.generateDerivedParameters(
                    KEY_LENGTH
                ) as KeyParameter

            Base64.encodeToString(
                keyParameter.key,
                Base64.NO_WRAP
            )

        } finally {

            passwordChars.fill('\u0000')
            passwordBytes.fill(0)
        }
    }

    fun verifyPassword(
        password: String,
        salt: String,
        storedHash: String
    ): Boolean {

        val enteredHash = hashPassword(
            password,
            salt
        )

        return MessageDigest.isEqual(
            Base64.decode(
                enteredHash,
                Base64.NO_WRAP
            ),
            Base64.decode(
                storedHash,
                Base64.NO_WRAP
            )
        )
    }
}