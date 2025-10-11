package com.potaninpm.feature_home.data.mappers

import com.potaninpm.feature_home.data.remote.dto.nytDto.NYTimesArticleDto
import com.potaninpm.feature_home.domain.model.NewsArticle
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun NYTimesArticleDto.toDomainNews(): NewsArticle {
    val imageUrl = multimedia?.default?.url

    val outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy '|' HH:mm").withZone(ZoneId.systemDefault())
    val dateTime = OffsetDateTime.parse(pubDate)

    val formattedDate = dateTime.format(outputFormatter)

    return NewsArticle(
        id = id,
        title = headline.main,
        abstract = abstract ?: "",
        webUrl = webUrl,
        imageUrl = imageUrl,
        source = source,
        publishedAt = formattedDate
    )
}