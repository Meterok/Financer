package com.potaninpm.feature_auth.domain.repository

import android.net.Uri

interface AuthRepository {
    suspend fun register(email: String, password: String, fullName: String, avatarUri: Uri?): Boolean
    suspend fun login(email: String, password: String): Boolean
    suspend fun signInWithYandex(token: String): Boolean

    fun getFcmToken(): String
    fun saveFcmToken(token: String)

    fun saveToken(token: String)
    fun getToken(): String?
    fun clearToken()
}
