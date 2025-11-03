package com.potaninpm.feature_finances.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GoalDto(
    @SerializedName("id") val id: Long,
    @SerializedName("user_id") val userId: Long,
    @SerializedName("name") val name: String,
    @SerializedName("target_amount") val targetAmount: Double,
    @SerializedName("current_amount") val currentAmount: Double,
    @SerializedName("deadline") val deadline: String?, // ISO 8601 date
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)

data class CreateGoalRequest(
    @SerializedName("name") val name: String,
    @SerializedName("target_amount") val targetAmount: Double,
    @SerializedName("current_amount") val currentAmount: Double = 0.0,
    @SerializedName("deadline") val deadline: String? = null
)

data class UpdateGoalRequest(
    @SerializedName("name") val name: String,
    @SerializedName("target_amount") val targetAmount: Double,
    @SerializedName("current_amount") val currentAmount: Double,
    @SerializedName("deadline") val deadline: String?
)
