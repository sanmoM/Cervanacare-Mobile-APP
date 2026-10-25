package com.example.data.security

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import com.example.data.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.nio.charset.StandardCharsets

data class UserSession(
    val isLoggedIn: Boolean = false,
    val token: String? = null,
    val role: UserRole = UserRole.CUSTOMER,
    val email: String? = null,
    val name: String? = null,
    val specialistId: String? = null
)

/**
 * Manages encrypted and secure on-device session state for Cervanna Care.
 * Encapsulates token storage, role-based authorization tokens, and immediate routing identity.
 */
class SecureSessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _sessionState = MutableStateFlow(loadInitialSession())
    val sessionState: StateFlow<UserSession> = _sessionState.asStateFlow()

    private fun loadInitialSession(): UserSession {
        val encryptedToken = prefs.getString(KEY_TOKEN, null)
        val roleStr = prefs.getString(KEY_ROLE, null)
        val email = prefs.getString(KEY_EMAIL, null)
        val name = prefs.getString(KEY_NAME, null)
        val specId = prefs.getString(KEY_SPEC_ID, null)

        val token = if (encryptedToken != null) decryptString(encryptedToken) else null
        val role = when (roleStr) {
            "SPECIALIST" -> UserRole.SPECIALIST
            else -> UserRole.CUSTOMER
        }

        return if (!token.isNullOrBlank()) {
            UserSession(
                isLoggedIn = true,
                token = token,
                role = role,
                email = email ?: "user@cervannacare.com",
                name = name ?: if (role == UserRole.CUSTOMER) "Sarah Kamau" else "Faith Muthoni, RN",
                specialistId = specId ?: "spec_1"
            )
        } else {
            UserSession(isLoggedIn = false)
        }
    }

    fun saveSession(
        token: String,
        role: UserRole,
        email: String,
        name: String,
        specialistId: String? = null
    ) {
        val encryptedToken = encryptString(token)
        prefs.edit()
            .putString(KEY_TOKEN, encryptedToken)
            .putString(KEY_ROLE, role.name)
            .putString(KEY_EMAIL, email)
            .putString(KEY_NAME, name)
            .putString(KEY_SPEC_ID, specialistId ?: if (role == UserRole.SPECIALIST) "spec_1" else null)
            .apply()

        _sessionState.value = UserSession(
            isLoggedIn = true,
            token = token,
            role = role,
            email = email,
            name = name,
            specialistId = specialistId ?: if (role == UserRole.SPECIALIST) "spec_1" else null
        )
    }

    fun clearSession() {
        prefs.edit().clear().apply()
        _sessionState.value = UserSession(isLoggedIn = false)
    }

    private fun encryptString(input: String): String {
        val bytes = input.toByteArray(StandardCharsets.UTF_8)
        // Obfuscate with simple XOR masking and Base64 encoding
        val masked = ByteArray(bytes.size)
        for (i in bytes.indices) {
            masked[i] = (bytes[i].toInt() xor XOR_KEY).toByte()
        }
        return Base64.encodeToString(masked, Base64.NO_WRAP)
    }

    private fun decryptString(encrypted: String): String {
        return try {
            val bytes = Base64.decode(encrypted, Base64.NO_WRAP)
            val unmasked = ByteArray(bytes.size)
            for (i in bytes.indices) {
                unmasked[i] = (bytes[i].toInt() xor XOR_KEY).toByte()
            }
            String(unmasked, StandardCharsets.UTF_8)
        } catch (e: Exception) {
            ""
        }
    }

    companion object {
        private const val PREFS_NAME = "cervanna_secure_store_prefs"
        private const val KEY_TOKEN = "sec_auth_token"
        private const val KEY_ROLE = "sec_user_role"
        private const val KEY_EMAIL = "sec_user_email"
        private const val KEY_NAME = "sec_user_name"
        private const val KEY_SPEC_ID = "sec_spec_id"
        private const val XOR_KEY = 0x5C
    }
}
