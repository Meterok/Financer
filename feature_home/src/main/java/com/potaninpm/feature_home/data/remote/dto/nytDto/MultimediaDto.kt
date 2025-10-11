package com.potaninpm.feature_home.data.remote.dto.nytDto

import com.google.gson.annotations.SerializedName

data class MultimediaDto(
    @SerializedName("default") val default: ImageDto?,
    @SerializedName("thumbail") val thumbnail: ImageDto?
)