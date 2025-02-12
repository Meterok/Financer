package com.potaninpm.data.remote.dto.nytDto

import com.google.gson.annotations.SerializedName

data class NYTimesResponse(
    @SerializedName("docs") val docs: List<NYTimesArticleDto>
)
