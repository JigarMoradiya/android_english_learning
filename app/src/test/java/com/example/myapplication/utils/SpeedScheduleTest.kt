package com.example.myapplication.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class SpeedScheduleTest {

    @Test
    fun startsSlowAtThreeSeconds() {
        assertEquals(3000L, SpeedSchedule.durationMillis(0))
    }

    @Test
    fun speedsUpByAQuarterSecondPerWord() {
        assertEquals(2750L, SpeedSchedule.durationMillis(1))
        assertEquals(2000L, SpeedSchedule.durationMillis(4))
    }

    @Test
    fun neverGoesBelowOneSecond() {
        assertEquals(1000L, SpeedSchedule.durationMillis(8))
        assertEquals(1000L, SpeedSchedule.durationMillis(50))
    }
}
