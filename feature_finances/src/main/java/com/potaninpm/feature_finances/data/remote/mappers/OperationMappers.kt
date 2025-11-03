package com.potaninpm.feature_finances.data.remote.mappers

import com.potaninpm.feature_finances.data.local.entities.OperationEntity
import com.potaninpm.feature_finances.data.remote.dto.CreateOperationRequest
import com.potaninpm.feature_finances.data.remote.dto.OperationDto
import com.potaninpm.feature_finances.data.remote.dto.UpdateOperationRequest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

// ISO 8601 datetime formatter
private val isoDateTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
    timeZone = TimeZone.getTimeZone("UTC")
}

// Конвертация OperationDto (из API) -> OperationEntity (для Room)
fun OperationDto.toEntity(): OperationEntity {
    val dateMillis = try {
        isoDateTimeFormat.parse(date)?.time ?: System.currentTimeMillis()
    } catch (e: Exception) {
        System.currentTimeMillis()
    }
    
    return OperationEntity(
        id = id,
        date = dateMillis,
        title = category, // Категория как title
        goalId = 0, // API не хранит goalId, используем 0 по умолчанию
        type = type,
        currency = "₽", // По умолчанию рубли
        amount = amount,
        comment = comment
    )
}

// Конвертация OperationEntity (из Room) -> CreateOperationRequest (для создания в API)
fun OperationEntity.toCreateRequest(): CreateOperationRequest {
    val dateString = isoDateTimeFormat.format(Date(date))
    
    return CreateOperationRequest(
        type = type,
        category = title, // title = category
        amount = amount,
        comment = comment,
        date = dateString
    )
}

// Конвертация OperationEntity (из Room) -> UpdateOperationRequest (для обновления в API)
fun OperationEntity.toUpdateRequest(): UpdateOperationRequest {
    val dateString = isoDateTimeFormat.format(Date(date))
    
    return UpdateOperationRequest(
        type = type,
        category = title,
        amount = amount,
        comment = comment,
        date = dateString
    )
}
