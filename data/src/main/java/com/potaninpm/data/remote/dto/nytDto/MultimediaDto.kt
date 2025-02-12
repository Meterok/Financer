package com.potaninpm.data.remote.dto.nytDto

import com.google.gson.annotations.SerializedName

data class MultimediaDto(
    @SerializedName("url") val url: String,
    @SerializedName("subtype") val subtype: String
)