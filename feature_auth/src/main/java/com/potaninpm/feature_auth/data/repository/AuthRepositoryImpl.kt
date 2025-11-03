package com.potaninpm.feature_auth.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.potaninpm.feature_auth.data.remote.api.AuthApi
import com.potaninpm.feature_auth.data.remote.dto.AuthResponse
import com.potaninpm.feature_auth.data.remote.dto.LoginRequestDto
import com.potaninpm.feature_auth.data.remote.dto.NotificationsRequestDto
import com.potaninpm.feature_auth.data.remote.dto.RegisterRequestDto
import com.potaninpm.feature_auth.data.remote.dto.YandexToken
import com.potaninpm.feature_auth.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

private const val TAG = "INFOGALL"
private const val JWT_TOKEN_KEY = "jwt_token"
private const val FCM_TOKEN_KEY = "fcm_token"

class AuthRepositoryImpl (
    private val api: AuthApi,
    private val prefs: SharedPreferences,
    private val context: Context
) : AuthRepository {
    override suspend fun register(email: String, password: String, fullName: String, avatarUri: Uri?): Boolean {
        return try {
            // Регистрация пользователя
            api.register(RegisterRequestDto(email = email, password = password))
            
            // Автоматический вход после регистрации
            val loginResponse = api.login(LoginRequestDto(email, password))
            val result = handleAuthResponse(loginResponse)

            result
        } catch (e: Exception) {
            Log.e(TAG, "Registration failed", e)
            false
        }
    }

    override suspend fun login(email: String, password: String): Boolean {
        return try {
            val response = api.login(LoginRequestDto(email, password))
            val result = handleAuthResponse(response)

            result
        } catch (e: Exception) {
            Log.e(TAG, "Login failed", e)
            false
        }
    }

    override suspend fun signInWithYandex(token: String): Boolean {
        return try {
            val response = api.signInWithYandex(YandexToken(token))
            val result = handleAuthResponse(response)

            result
        } catch (e: Exception) {
            Log.e(TAG, "Yandex sign-in failed", e)
            false
        }
    }

    override fun getFcmToken(): String {
        return prefs.getString(FCM_TOKEN_KEY, null) ?: throw IllegalStateException("FCM token is null")
    }

    override fun saveFcmToken(token: String) {
        prefs.edit().putString(FCM_TOKEN_KEY, token).apply()
    }

    private fun handleAuthResponse(response: AuthResponse): Boolean {
        saveToken(response.token)
        return true
    }

    override fun saveToken(token: String) {
        prefs.edit().putString(JWT_TOKEN_KEY, token).apply()
    }

    override fun getToken(): String? {
        return prefs.getString(JWT_TOKEN_KEY, null)
    }

    override fun clearToken() {
        prefs.edit().remove(JWT_TOKEN_KEY).apply()
    }

    private suspend fun loadImage(uri: Uri): String? = withContext(Dispatchers.IO) {
        try {
            val tempFile = createTempFileFromUri(uri) ?: return@withContext null
            return@withContext uploadFileToServer(tempFile)
        } catch (e: Exception) {
            Log.e(TAG, "Error loading image", e)
            return@withContext null
        }
    }

    private suspend fun createTempFileFromUri(uri: Uri): File? {
        return try {
            val tempFile = File.createTempFile("avatar_", ".jpg")

            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            } ?: return null

            tempFile
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create temp file from URI", e)
            null
        }
    }

    private suspend fun uploadFileToServer(file: File): String {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())

        val multipartFile = MultipartBody.Part.createFormData(
            "file",
            file.name,
            requestFile
        )

        val response = api.uploadFile(file = multipartFile)
        return response.url
    }
}
