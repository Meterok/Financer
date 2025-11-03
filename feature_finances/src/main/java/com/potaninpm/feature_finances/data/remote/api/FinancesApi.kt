package com.potaninpm.feature_finances.data.remote.api

import com.potaninpm.feature_finances.data.remote.dto.CreateGoalRequest
import com.potaninpm.feature_finances.data.remote.dto.CreateOperationRequest
import com.potaninpm.feature_finances.data.remote.dto.GoalDto
import com.potaninpm.feature_finances.data.remote.dto.OperationDto
import com.potaninpm.feature_finances.data.remote.dto.UpdateGoalRequest
import com.potaninpm.feature_finances.data.remote.dto.UpdateOperationRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface FinancesApi {
    
    // ========== Goals API ==========
    
    @GET("goals")
    suspend fun getGoals(): List<GoalDto>
    
    @POST("goals")
    suspend fun createGoal(@Body request: CreateGoalRequest): GoalDto
    
    @PUT("goals/{goal_id}")
    suspend fun updateGoal(
        @Path("goal_id") goalId: Long,
        @Body request: UpdateGoalRequest
    ): GoalDto
    
    @DELETE("goals/{goal_id}")
    suspend fun deleteGoal(@Path("goal_id") goalId: Long)
    
    // ========== Operations API ==========
    
    @GET("operations")
    suspend fun getOperations(
        @Query("type") type: String? = null,
        @Query("category") category: String? = null,
        @Query("from_date") fromDate: String? = null,
        @Query("to_date") toDate: String? = null,
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0
    ): List<OperationDto>
    
    @POST("operations")
    suspend fun createOperation(@Body request: CreateOperationRequest): OperationDto
    
    @PUT("operations/{operation_id}")
    suspend fun updateOperation(
        @Path("operation_id") operationId: Long,
        @Body request: UpdateOperationRequest
    ): OperationDto
    
    @DELETE("operations/{operation_id}")
    suspend fun deleteOperation(@Path("operation_id") operationId: Long)
}
