package com.potaninpm.feature_finances.data.remote.dto

import com.google.gson.annotations.SerializedName

data class OperationDto(
    @SerializedName("id") val id: Long,
    @SerializedName("user_id") val userId: Long,
    @SerializedName("type") val type: String, // "income" or "expense"
    @SerializedName("category") val category: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("comment") val comment: String?,
    @SerializedName("date") val date: String, // ISO 8601 datetime
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)

data class CreateOperationRequest(
    @SerializedName("type") val type: String, // "income" or "expense"
    @SerializedName("category") val category: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("comment") val comment: String?,
    @SerializedName("date") val date: String // ISO 8601 datetime
)

data class UpdateOperationRequest(
    @SerializedName("type") val type: String,
    @SerializedName("category") val category: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("comment") val comment: String?,
    @SerializedName("date") val date: String
)
