package com.potaninpm.feature_auth.data.remote.api

import com.potaninpm.feature_auth.data.remote.dto.AuthResponse
import com.potaninpm.feature_auth.data.remote.dto.FileUploadResponse
import com.potaninpm.feature_auth.data.remote.dto.LoginRequestDto
import com.potaninpm.feature_auth.data.remote.dto.NotificationsRequestDto
import com.potaninpm.feature_auth.data.remote.dto.RegisterRequestDto
import com.potaninpm.feature_auth.data.remote.dto.YandexToken
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthApi {
    @POST("register")
    suspend fun register(@Body request: RegisterRequestDto): com.potaninpm.feature_auth.data.remote.dto.RegisterResponseDto

    @Multipart
    @POST("storage/upload")
    suspend fun uploadFile(
        @Part file: MultipartBody.Part
    ): FileUploadResponse

    @POST("login")
    suspend fun login(@Body request: LoginRequestDto): AuthResponse

    @POST("auth/login/yandex")
    suspend fun signInWithYandex(@Body token: YandexToken): AuthResponse

    @POST("notifications/device-tokens")
    suspend fun sendDeviceToken(@Body info: NotificationsRequestDto)
}