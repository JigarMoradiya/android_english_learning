package com.example.myapplication.data.notifications

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

private const val UNIQUE_WORK_NAME = "streak_reminder"
private const val REMINDER_HOUR = 19 // 7:00 PM local

/**
 * Schedules [StreakReminderWorker] to run roughly once a day around 7 PM local time.
 *
 * Uses WorkManager's periodic work rather than an exact AlarmManager alarm: a once-daily
 * nudge doesn't need to-the-minute precision, exact alarms need extra permission and Play
 * Store scrutiny on Android 12+, and WorkManager already survives reboots and respects
 * Doze gracefully. Trade-off: under aggressive battery optimization the actual fire time
 * can drift by up to roughly an hour from the target — acceptable here.
 */
@Singleton
class StreakReminderScheduler @Inject constructor() {

    /** Safe to call repeatedly (app start, every activity completion) — WorkManager
     *  dedupes by [UNIQUE_WORK_NAME], so this never enqueues more than one pending job. */
    fun scheduleDaily(context: Context) {
        val request = PeriodicWorkRequestBuilder<StreakReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(millisUntilNextReminderHour(), TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            UNIQUE_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    private fun millisUntilNextReminderHour(): Long {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, REMINDER_HOUR)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        if (!target.after(now)) {
            target.add(Calendar.DAY_OF_YEAR, 1)
        }
        return target.timeInMillis - now.timeInMillis
    }
}
