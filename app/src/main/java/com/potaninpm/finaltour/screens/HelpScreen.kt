package com.potaninpm.finaltour.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.potaninpm.feature_auth.presentation.viewModels.AuthViewModel
import com.potaninpm.finaltour.R
import com.potaninpm.finaltour.navigation.RootNavDestinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Помощь",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            authViewModel.logout()
                            navController.navigate(RootNavDestinations.Auth.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.logout_24px),
                            contentDescription = "Выход",
                            tint = Color.Red
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            HelpSection(
                title = "🏠 Главная",
                content = """
                    На главном экране вы можете:
                    • Просматривать последние новости из мира финансов
                    • Следить за котировками акций и криптовалют
                    • Нажмите на карточку новости, чтобы прочитать полную статью
                    • Используйте кнопку поиска в верхней части экрана для поиска тикеров
                    • Нажмите на карточку тикера, чтобы увидеть подробную информацию
                """.trimIndent()
            )

            Spacer(modifier = Modifier.height(12.dp))

            HelpSection(
                title = "💰 Финансы",
                content = """
                    Управляйте своими финансами:
                    
                    📊 Статистика:
                    • В верхней карточке отображается ваш баланс и статистика доходов/расходов
                    
                    🎯 Цели:
                    • Нажмите кнопку "+" рядом с заголовком "Цели", чтобы создать новую цель
                    • Укажите название, целевую сумму и срок достижения
                    • На карточке цели нажмите кнопку меню (три точки) для пополнения или снятия средств
                    • Следите за прогрессом достижения цели в процентах
                    
                    💸 Операции:
                    • Нажмите кнопку "+" рядом с заголовком "Операции", чтобы добавить доход или расход
                    • Выберите тип операции (доход/расход), категорию и сумму
                    • Добавьте комментарий для удобства отслеживания
                    • Все операции группируются по датам
                """.trimIndent()
            )

            Spacer(modifier = Modifier.height(12.dp))

            HelpSection(
                title = "🤖 AI Помощник",
                content = """
                    Используйте искусственный интеллект:
                    • На главном экране нажмите на тикер
                    • Задавайте вопросы о финансах и инвестициях
                    • Получайте персонализированные советы
                    • Анализируйте рыночные тренды с помощью AI
                """.trimIndent()
            )

            Spacer(modifier = Modifier.height(12.dp))

            HelpSection(
                title = "⚙️ Дополнительно",
                content = """
                    Полезные советы:
                    • Регулярно обновляйте свои финансовые данные для точной статистики
                    • Используйте цели для мотивации накоплений
                    • Следите за новостями, чтобы быть в курсе рыночных изменений
                    
                    📧 Нужна помощь? Свяжитесь с нами:
                    • Email: support@financer.app
                    • Telegram: @financer_support
                """.trimIndent()
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun HelpSection(
    title: String,
    content: String
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight.times(1.5f)
            )
        }
    }
}
