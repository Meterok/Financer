package com.potaninpm.feature_finances.presentation.components.operations.section

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.potaninpm.feature_finances.domain.model.Operation
import com.potaninpm.feature_finances.presentation.screens.AddButton
import com.potaninpm.feature_finances.presentation.screens.groupOperationsByDate
import com.potaninpm.feature_finances.R

@Composable
fun OperationsSection(
    operations: List<Operation>,
    onAddOperationClick: () -> Unit
) {
    val grouped = groupOperationsByDate(operations)
        .toList()
        .sortedByDescending { it.first }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        AddButton(
            onAddClick = onAddOperationClick,
            title = R.string.add_operation
        )

        grouped.forEach { (date, ops) ->
            DateHeader(date = date)

            ops.forEach { operation ->
                OperationItem(
                    operation = operation,
                    icon = {
                        if (operation.amount >= 0) {
                            Icon(
                                painter = painterResource(R.drawable.add_24px),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .padding(8.dp)
                            )
                        } else {
                            Icon(
                                painter = painterResource(R.drawable.remove_24px),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .padding(8.dp)
                            )
                        }
                    }
                )
            }
        }
    }
}
