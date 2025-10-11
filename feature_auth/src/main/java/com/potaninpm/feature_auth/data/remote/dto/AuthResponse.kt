package com.potaninpm.feature_auth.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("access_token") val token: String
)
