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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HelpScreen(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "Помощь",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

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
