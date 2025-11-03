package com.potaninpm.feature_finances.data.repository

import android.util.Log
import com.potaninpm.feature_finances.data.local.database.FinanceDatabase
import com.potaninpm.feature_finances.data.local.entities.OperationEntity
import com.potaninpm.feature_finances.data.remote.api.FinancesApi
import com.potaninpm.feature_finances.data.remote.mappers.toCreateRequest
import com.potaninpm.feature_finances.data.remote.mappers.toEntity
import com.potaninpm.feature_finances.data.remote.mappers.toUpdateRequest
import kotlinx.coroutines.flow.Flow

class OperationsRepository(
    private val db: FinanceDatabase,
    private val api: FinancesApi
) {
    private val TAG = "OperationsRepository"

    // Локальные методы (работа с Room)
    fun getOperations(): Flow<List<OperationEntity>> = db.operationDao().getOperations()

    suspend fun addOperation(operation: OperationEntity): Long = db.operationDao().insertOperation(operation)
    
    suspend fun updateOperation(operation: OperationEntity) = db.operationDao().updateOperation(operation)
    
    suspend fun deleteOperation(operationId: Long) = db.operationDao().deleteOperationById(operationId)

    // Синхронизация с сервером
    
    /**
     * Загрузить все операции с сервера и сохранить локально
     */
    suspend fun syncOperationsFromServer() {
        try {
            val operationsFromServer = api.getOperations()
            val entities = operationsFromServer.map { it.toEntity() }
            
            // Очищаем и вставляем новые
            db.operationDao().clearAll()
            entities.forEach { db.operationDao().insertOperation(it) }
            
            Log.d(TAG, "Successfully synced ${entities.size} operations from server")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to sync operations from server", e)
            throw e
        }
    }
    
    /**
     * Отправить операцию на сервер (создать или обновить)
     */
    suspend fun syncOperationToServer(operation: OperationEntity): OperationEntity {
        return try {
            if (operation.id == 0L) {
                // Создать новую операцию на сервере
                val response = api.createOperation(operation.toCreateRequest())
                val newEntity = response.toEntity()
                // Обновить локальную запись с ID с сервера
                db.operationDao().updateOperation(newEntity)
                Log.d(TAG, "Created operation on server with ID: ${response.id}")
                newEntity
            } else {
                // Обновить существующую операцию
                val response = api.updateOperation(operation.id, operation.toUpdateRequest())
                Log.d(TAG, "Updated operation on server: ${operation.id}")
                response.toEntity()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to sync operation to server", e)
            throw e
        }
    }
    
    /**
     * Удалить операцию на сервере
     */
    suspend fun deleteOperationOnServer(operationId: Long) {
        try {
            api.deleteOperation(operationId)
            Log.d(TAG, "Deleted operation on server: $operationId")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to delete operation on server", e)
            throw e
        }
    }
    
    /**
     * Добавить операцию и синхронизировать с сервером
     */
    suspend fun addOperationWithSync(operation: OperationEntity): Long {
        return try {
            // 1. Создать на сервере
            val serverOperation = api.createOperation(operation.toCreateRequest())
            
            // 2. Сохранить локально с ID от сервера
            val entity = serverOperation.toEntity()
            db.operationDao().insertOperation(entity)
            
            Log.d(TAG, "Added operation with sync: ${entity.id}")
            entity.id
        } catch (e: Exception) {
            Log.e(TAG, "Failed to add operation with sync, saving locally only", e)
            // При ошибке сохраняем только локально
            db.operationDao().insertOperation(operation)
        }
    }
    
    /**
     * Обновить операцию и синхронизировать с сервером
     */
    suspend fun updateOperationWithSync(operation: OperationEntity) {
        try {
            // 1. Обновить локально
            db.operationDao().updateOperation(operation)
            
            // 2. Синхронизировать с сервером
            if (operation.id != 0L) {
                api.updateOperation(operation.id, operation.toUpdateRequest())
                Log.d(TAG, "Updated operation with sync: ${operation.id}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to update operation on server", e)
            // Локальные изменения уже сохранены
        }
    }
    
    /**
     * Удалить операцию локально и на сервере
     */
    suspend fun deleteOperationWithSync(operationId: Long) {
        try {
            // 1. Удалить локально
            db.operationDao().deleteOperationById(operationId)
            
            // 2. Удалить на сервере
            if (operationId != 0L) {
                api.deleteOperation(operationId)
                Log.d(TAG, "Deleted operation with sync: $operationId")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to delete operation on server", e)
            // Локальное удаление уже выполнено
        }
    }
}