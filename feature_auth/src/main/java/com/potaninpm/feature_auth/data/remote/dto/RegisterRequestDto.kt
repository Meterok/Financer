package com.potaninpm.feature_auth.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterRequestDto(
    val email: String,
    @SerializedName("full_name") val fullName: String,
    val password: String,
    @SerializedName("avatar_url") val avatarUrl: String?
)
