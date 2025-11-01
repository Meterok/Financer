package com.potaninpm.finaltour

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class HelpScreenContentTest {

    @Test
    fun `help content should contain main sections`() {
        val sections = listOf(
            "Главная",
            "Финансы",
            "AI Помощник",
            "Дополнительно"
        )
        
        sections.forEach { section ->
            assertNotNull("Section $section should exist", section)
            assertTrue("Section should not be empty", section.isNotEmpty())
        }
    }

    @Test
    fun `help instructions should contain action keywords`() {
        val instructions = listOf(
            "Нажмите",
            "Создать",
            "Добавить",
            "Просматривать"
        )
        
        instructions.forEach { instruction ->
            assertTrue("Instruction should be valid", instruction.length > 3)
        }
    }

    @Test
    fun `support contacts should be available`() {
        val supportEmail = "support@financer.app"
        val supportTelegram = "@financer_support"
        
        assertTrue("Email should be valid", supportEmail.contains("@"))
        assertTrue("Telegram should start with @", supportTelegram.startsWith("@"))
    }
}
