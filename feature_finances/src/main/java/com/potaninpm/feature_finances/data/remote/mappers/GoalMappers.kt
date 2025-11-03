package com.potaninpm.feature_finances.data.remote.mappers

import com.potaninpm.feature_finances.data.local.entities.GoalEntity
import com.potaninpm.feature_finances.data.remote.dto.CreateGoalRequest
import com.potaninpm.feature_finances.data.remote.dto.GoalDto
import com.potaninpm.feature_finances.data.remote.dto.UpdateGoalRequest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

// ISO 8601 formatter
private val isoDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
    timeZone = TimeZone.getTimeZone("UTC")
}

// Конвертация GoalDto (из API) -> GoalEntity (для Room)
fun GoalDto.toEntity(): GoalEntity {
    val dueDateMillis = deadline?.let {
        try {
            isoDateFormat.parse(it)?.time
        } catch (e: Exception) {
            null
        }
    }
    
    return GoalEntity(
        id = id,
        title = name,
        targetAmount = targetAmount.toLong(),
        currentAmount = currentAmount.toLong(),
        currency = "₽", // По умолчанию рубли, можно добавить в API
        dueDate = dueDateMillis
    )
}

// Конвертация GoalEntity (из Room) -> CreateGoalRequest (для создания в API)
fun GoalEntity.toCreateRequest(): CreateGoalRequest {
    val deadlineString = dueDate?.let {
        isoDateFormat.format(Date(it))
    }
    
    return CreateGoalRequest(
        name = title,
        targetAmount = targetAmount.toDouble(),
        currentAmount = currentAmount.toDouble(),
        deadline = deadlineString
    )
}

// Конвертация GoalEntity (из Room) -> UpdateGoalRequest (для обновления в API)
fun GoalEntity.toUpdateRequest(): UpdateGoalRequest {
    val deadlineString = dueDate?.let {
        isoDateFormat.format(Date(it))
    }
    
    return UpdateGoalRequest(
        name = title,
        targetAmount = targetAmount.toDouble(),
        currentAmount = currentAmount.toDouble(),
        deadline = deadlineString
    )
}
