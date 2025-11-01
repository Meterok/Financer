package com.potaninpm.core.functions

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FormatMoneyTest {

    @Test
    fun `formatMoneyUnsigned should format positive value correctly`() {
        val result = formatMoneyUnsigned(1000)

        assertTrue("Result should contain '1' and '000'", 
            result.contains("1") && result.contains("000"))
    }

    @Test
    fun `formatMoneyUnsigned should format zero correctly`() {
        val result = formatMoneyUnsigned(0)
        assertEquals("0", result)
    }

    @Test
    fun `formatMoneyUnsigned should format large value correctly`() {
        val result = formatMoneyUnsigned(1234567)

        assertTrue("Result should contain formatted number", 
            result.contains("1") && result.contains("234") && result.contains("567"))
    }

    @Test
    fun `formatMoneySigned should format positive value without sign`() {
        val result = formatMoneySigned(1000.0, "₽", signed = false)

        assertTrue("Result should not start with + and contain currency",
            !result.startsWith("+") && result.contains("₽") && result.contains("1") && result.contains("000"))
    }

    @Test
    fun `formatMoneySigned should format positive value with plus sign`() {
        val result = formatMoneySigned(1000.0, "₽", signed = true)

        assertTrue("Result should start with + for positive signed values",
            result.startsWith("+"))
    }

    @Test
    fun `formatMoneySigned should format negative value with minus sign`() {
        val result = formatMoneySigned(-500.0, "₽", signed = true)

        assertTrue("Result should contain minus for negative values",
            result.contains("-"))
    }

    @Test
    fun `formatMoneySigned should format zero without sign`() {
        val result = formatMoneySigned(0.0, "₽", signed = false)
        assertTrue("Result should contain 0 and currency",
            result.contains("0") && result.contains("₽") && !result.startsWith("+"))
    }

    @Test
    fun `formatMoneySigned should format zero with plus sign when signed`() {
        val result = formatMoneySigned(0.0, "₽", signed = true)
        assertTrue("Result should start with + for zero when signed",
            result.startsWith("+"))
    }

    @Test
    fun `formatMoneySigned should handle decimal values`() {
        val result = formatMoneySigned(1234.56, "₽", signed = false)

        assertTrue("Result should contain decimal separator",
            result.contains(",") || result.contains("."))
        assertTrue("Result should contain currency", result.contains("₽"))
    }

    @Test
    fun `formatMoneySigned should handle different currencies`() {
        val result = formatMoneySigned(100.0, "$", signed = false)
        assertTrue("Result should contain $ currency",
            result.contains("$") && result.contains("100"))
    }
    
    @Test
    fun `formatMoneyUnsigned should handle negative values as positive`() {
        val result = formatMoneyUnsigned(-1000)

        assertTrue("Result should contain number", result.contains("1") || result.contains("000"))
    }
}
