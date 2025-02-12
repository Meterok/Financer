package com.potaninpm.domain.model

data class Ticker(
    val symbol: String,
    val companyName: String?,
    val currentPrice: Float,
    val change: Float,
    val changePercent: Float,
    val logoUrl: String?
)
