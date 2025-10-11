package com.potaninpm.feature_auth.data.remote.api.interceptors

import android.content.SharedPreferences
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val prefs: SharedPreferences
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request().newBuilder().apply {
            header("accept", "application/json")
            header("Content-Type", "application/json")
        }.build()

        if (originalRequest.url.encodedPath.startsWith("/users") ||
            originalRequest.url.encodedPath.startsWith("/auth") &&
            !originalRequest.url.encodedPath.startsWith("/auth/me")
            ) {

            return chain.proceed(originalRequest)
        }

        val token = prefs.getString("jwt_token", null)

        Log.i("INFOG", token.toString())

        val newRequest = originalRequest.newBuilder().apply {
            header("accept", "application/json")
            header("Content-Type", "application/json")
            token?.let {
                header("Authorization", "Bearer $it")
            }
        }.build()

        return chain.proceed(newRequest)
    }
}

