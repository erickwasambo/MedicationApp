package com.wasambo.medication

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GreetingTest {

    @Test
    fun testMorningGreeting() {
        val greeting = getGreeting(9) // 9 AM
        assertEquals("Good Morning", greeting)
    }

    @Test
    fun testAfternoonGreeting() {
        val greeting = getGreeting(14) // 2 PM
        assertEquals("Good Afternoon", greeting)
    }

    @Test
    fun testEveningGreeting() {
        val greeting = getGreeting(19) // 7 PM
        assertEquals("Good Evening", greeting)
    }

    @Test
    fun testNightGreeting() {
        val greeting = getGreeting(23) // 11 PM
        assertEquals("Good Night", greeting)
    }

    private fun getGreeting(hour: Int): String {
        return when (hour) {
            in 5..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..20 -> "Good Evening"
            else -> "Good Night"
        }
    }
} 