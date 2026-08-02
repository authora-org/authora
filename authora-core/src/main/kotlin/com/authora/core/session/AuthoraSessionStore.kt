package com.authora.core.session

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AuthoraSessionStore(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "authora_sessions",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val json = Json { ignoreUnknownKeys = true }

    fun getAllSessions(): List<AuthoraSession> {
        val raw = prefs.getString(KEY_SESSIONS, null) ?: return emptyList()
        return try {
            json.decodeFromString<List<AuthoraSession>>(raw)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getActiveSession(): AuthoraSession? = getAllSessions().firstOrNull { it.isActive }

    fun saveSession(session: AuthoraSession) {
        val current = getAllSessions().filterNot { it.userId == session.userId }
        val updated = if (session.isActive) {
            current.map { it.copy(isActive = false) } + session
        } else {
            current + session
        }
        persist(updated)
    }

    fun setActiveSession(userId: String) {
        val updated = getAllSessions().map { it.copy(isActive = it.userId == userId) }
        persist(updated)
    }

    fun removeSession(userId: String) {
        val remaining = getAllSessions().filterNot { it.userId == userId }
        val updated = if (remaining.isNotEmpty() && remaining.none { it.isActive }) {
            remaining.mapIndexed { index, session -> if (index == 0) session.copy(isActive = true) else session }
        } else {
            remaining
        }
        persist(updated)
    }

    fun clear() {
        prefs.edit().remove(KEY_SESSIONS).apply()
    }

    private fun persist(sessions: List<AuthoraSession>) {
        prefs.edit().putString(KEY_SESSIONS, json.encodeToString(sessions)).apply()
    }

    private companion object {
        const val KEY_SESSIONS = "sessions"
    }
}