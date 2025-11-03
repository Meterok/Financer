package com.potaninpm.feature_finances.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potaninpm.feature_finances.data.local.entities.GoalEntity
import com.potaninpm.feature_finances.data.local.entities.OperationEntity
import com.potaninpm.feature_finances.data.mappers.toDomain
import com.potaninpm.feature_finances.data.repository.GoalsRepository
import com.potaninpm.feature_finances.data.repository.OperationsRepository
import com.potaninpm.feature_finances.domain.Operation
import com.potaninpm.feature_finances.domain.OperationType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId

class FinancesViewModel(
    private val goalRepository: GoalsRepository,
    private val operationRepository: OperationsRepository
): ViewModel() {

    val goals: StateFlow<List<GoalEntity>> =
        goalRepository.getGoals().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val operations: StateFlow<List<Operation>> =
        operationRepository.getOperations()
            .map { list -> list.map { it.toDomain() } }
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        // Синхронизация данных с сервера при запуске
        syncDataFromServer()
    }

    /**
     * Загрузить данные с сервера
     */
    private fun syncDataFromServer() {
        viewModelScope.launch {
            try {
                // Параллельная загрузка целей и операций
                launch { 
                    try {
                        goalRepository.syncGoalsFromServer()
                    } catch (e: Exception) {
                        // Игнорируем ошибки синхронизации, используем локальные данные
                    }
                }
                launch { 
                    try {
                        operationRepository.syncOperationsFromServer()
                    } catch (e: Exception) {
                        // Игнорируем ошибки синхронизации, используем локальные данные
                    }
                }
            } catch (e: Exception) {
                // Общие ошибки игнорируются
            }
        }
    }

    /**
     * Принудительная синхронизация (для pull-to-refresh)
     */
    fun forceSync() {
        syncDataFromServer()
    }

    val totalSavings: StateFlow<Long> =
        goals.map { list -> list.sumOf { it.currentAmount } }
            .stateIn(viewModelScope, SharingStarted.Lazily, 0L)

    val totalTarget: StateFlow<Long> =
        goals.map { list -> list.sumOf { it.targetAmount } }
            .stateIn(viewModelScope, SharingStarted.Lazily, 0L)

    val averageMonthlyInflow: StateFlow<Double> =
        operations.map { ops ->
            val deposits = ops.filter { it.amount > 0 && it.type == OperationType.DEPOSIT.type }
            val groups: Map<Int, List<Operation>> = deposits.groupBy { op ->
                val date = Instant.ofEpochMilli(op.date)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                date.year * 100 + date.monthValue
            }
            if (groups.isNotEmpty()) {
                groups.values.map { group -> group.sumOf { it.amount } }.average()
            } else 0.0
        }
            .stateIn(viewModelScope, SharingStarted.Lazily, 0.0)

    fun addGoal(title: String, targetAmount: Long, currency: String, dueDate: Long?) {
        viewModelScope.launch {
            val goal = GoalEntity(
                title = title.trim(),
                targetAmount = targetAmount,
                currency = currency,
                dueDate = dueDate
            )
            // Добавляем с синхронизацией на сервер
            goalRepository.addGoalWithSync(goal)
        }
    }

    fun transferMoney(sourceGoal: GoalEntity, targetGoal: GoalEntity, amount: Long, comment: String?) {
        viewModelScope.launch {
            operationRepository.addOperationWithSync(
                OperationEntity(
                    title = sourceGoal.title + " -> " + targetGoal.title,
                    goalId = sourceGoal.id,
                    type = OperationType.TRANSFER.type,
                    currency = sourceGoal.currency,
                    amount = amount.toDouble(),
                    comment = comment
                )
            )

            val updatedSource = sourceGoal.copy(currentAmount = sourceGoal.currentAmount - amount)
            goalRepository.updateGoalWithSync(updatedSource)

            val updatedTarget = targetGoal.copy(currentAmount = targetGoal.currentAmount + amount)
            goalRepository.updateGoalWithSync(updatedTarget)
        }
    }


    fun deleteGoal(goal: GoalEntity) {
        viewModelScope.launch {
            if (goal.currentAmount > 0) {
                operationRepository.addOperationWithSync(
                    OperationEntity(
                        title = goal.title,
                        goalId = goal.id,
                        type = OperationType.DELETE.type,
                        currency = goal.currency,
                        amount = -goal.currentAmount.toDouble(),
                        comment = goal.title
                    )
                )
            }
            goalRepository.deleteGoalWithSync(goal.id)
        }
    }

    fun addDeposit(
        goal: GoalEntity,
        amount: Long,
        comment: String?
    ) {
        viewModelScope.launch {
            operationRepository.addOperationWithSync(
                OperationEntity(
                    title = goal.title,
                    goalId = goal.id,
                    type = OperationType.DEPOSIT.type,
                    currency = goal.currency,
                    amount = amount.toDouble(),
                    comment = comment
                )
            )

            val updatedGoal = goal.copy(currentAmount = goal.currentAmount + amount)
            goalRepository.updateGoalWithSync(updatedGoal)
        }
    }

    fun addWithdrawal(
        goal: GoalEntity,
        amount: Long,
        comment: String?
    ) {
        viewModelScope.launch {
            operationRepository.addOperationWithSync(
                OperationEntity(
                    title = goal.title,
                    goalId = goal.id,
                    type = OperationType.WITHDRAWAL.type,
                    currency = goal.currency,
                    amount = -amount.toDouble(),
                    comment = comment
                )
            )

            val updatedGoal = goal.copy(currentAmount = goal.currentAmount - amount)
            goalRepository.updateGoalWithSync(updatedGoal)
        }
    }
}