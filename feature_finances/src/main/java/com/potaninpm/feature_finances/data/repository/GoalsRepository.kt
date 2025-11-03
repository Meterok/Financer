package com.potaninpm.feature_finances.data.repository

import android.util.Log
import com.potaninpm.feature_finances.data.local.database.FinanceDatabase
import com.potaninpm.feature_finances.data.local.entities.GoalEntity
import com.potaninpm.feature_finances.data.remote.api.FinancesApi
import com.potaninpm.feature_finances.data.remote.mappers.toCreateRequest
import com.potaninpm.feature_finances.data.remote.mappers.toEntity
import com.potaninpm.feature_finances.data.remote.mappers.toUpdateRequest
import kotlinx.coroutines.flow.Flow

class GoalsRepository(
    private val db: FinanceDatabase,
    private val api: FinancesApi
) {
    private val TAG = "GoalsRepository"

    // Локальные методы (работа с Room)
    fun getGoals(): Flow<List<GoalEntity>> = db.goalDao().getGoals()

    suspend fun addGoal(goal: GoalEntity): Long = db.goalDao().insertGoal(goal)
    
    suspend fun updateGoal(goal: GoalEntity) = db.goalDao().updateGoal(goal)
    
    suspend fun deleteGoal(goal: GoalEntity) = db.goalDao().deleteGoal(goal)

    // Синхронизация с сервером
    
    /**
     * Загрузить все цели с сервера и сохранить локально
     */
    suspend fun syncGoalsFromServer() {
        try {
            val goalsFromServer = api.getGoals()
            val entities = goalsFromServer.map { it.toEntity() }
            
            // Очищаем и вставляем новые
            db.goalDao().clearAll()
            entities.forEach { db.goalDao().insertGoal(it) }
            
            Log.d(TAG, "Successfully synced ${entities.size} goals from server")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to sync goals from server", e)
            throw e
        }
    }
    
    /**
     * Отправить цель на сервер (создать или обновить)
     */
    suspend fun syncGoalToServer(goal: GoalEntity): GoalEntity {
        return try {
            if (goal.id == 0L) {
                // Создать новую цель на сервере
                val response = api.createGoal(goal.toCreateRequest())
                val newEntity = response.toEntity()
                // Обновить локальную запись с ID с сервера
                db.goalDao().updateGoal(newEntity)
                Log.d(TAG, "Created goal on server with ID: ${response.id}")
                newEntity
            } else {
                // Обновить существующую цель
                val response = api.updateGoal(goal.id, goal.toUpdateRequest())
                Log.d(TAG, "Updated goal on server: ${goal.id}")
                response.toEntity()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to sync goal to server", e)
            throw e
        }
    }
    
    /**
     * Удалить цель на сервере
     */
    suspend fun deleteGoalOnServer(goalId: Long) {
        try {
            api.deleteGoal(goalId)
            Log.d(TAG, "Deleted goal on server: $goalId")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to delete goal on server", e)
            throw e
        }
    }
    
    /**
     * Добавить цель и синхронизировать с сервером
     */
    suspend fun addGoalWithSync(goal: GoalEntity): Long {
        return try {
            // 1. Создать на сервере
            val serverGoal = api.createGoal(goal.toCreateRequest())
            
            // 2. Сохранить локально с ID от сервера
            val entity = serverGoal.toEntity()
            db.goalDao().insertGoal(entity)
            
            Log.d(TAG, "Added goal with sync: ${entity.id}")
            entity.id
        } catch (e: Exception) {
            Log.e(TAG, "Failed to add goal with sync, saving locally only", e)
            // При ошибке сохраняем только локально
            db.goalDao().insertGoal(goal)
        }
    }
    
    /**
     * Обновить цель и синхронизировать с сервером
     */
    suspend fun updateGoalWithSync(goal: GoalEntity) {
        try {
            // 1. Обновить локально
            db.goalDao().updateGoal(goal)
            
            // 2. Синхронизировать с сервером
            if (goal.id != 0L) {
                api.updateGoal(goal.id, goal.toUpdateRequest())
                Log.d(TAG, "Updated goal with sync: ${goal.id}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to update goal on server", e)
            // Локальные изменения уже сохранены
        }
    }
    
    /**
     * Удалить цель локально и на сервере
     */
    suspend fun deleteGoalWithSync(goalId: Long) {
        try {
            // 1. Удалить локально
            db.goalDao().deleteGoalById(goalId)
            
            // 2. Удалить на сервере
            if (goalId != 0L) {
                api.deleteGoal(goalId)
                Log.d(TAG, "Deleted goal with sync: $goalId")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to delete goal on server", e)
            // Локальное удаление уже выполнено
        }
    }
}