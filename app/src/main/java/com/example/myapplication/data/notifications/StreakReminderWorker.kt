package com.example.myapplication.data.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.myapplication.R
import com.example.myapplication.data.progress.StreakRepository
import com.example.myapplication.main.AgeCategoryActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

private const val CHANNEL_ID = "streak_reminders"
private const val NOTIFICATION_ID = 4821

/**
 * Runs roughly once a day (scheduled by [StreakReminderScheduler]). Posts a local
 * reminder only if the user hasn't completed an activity yet today — never fires
 * for an already-active user.
 *
 * The message rotates across activity types each day (see [ReminderMessages]) so the
 * single daily nudge stays fresh and encourages trying something new.
 */
@HiltWorker
class StreakReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val streakRepository: StreakRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val streak = streakRepository.streak.value
        if (streak.hasCompletedToday) return Result.success()

        val dayOfYear = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR)
        postReminder(streak.currentStreak, dayOfYear)
        return Result.success()
    }

    private fun postReminder(currentStreak: Int, dayOfYear: Int) {
        val context = applicationContext
        createChannelIfNeeded(context)

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val (title, body) = ReminderMessages.forToday(currentStreak, dayOfYear)

        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            ?: android.content.Intent(context, AgeCategoryActivity::class.java)
        val contentIntent = android.app.PendingIntent.getActivity(
            context, 0, launchIntent,
            android.app.PendingIntent.FLAG_IMMUTABLE or android.app.PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.small_icon)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(contentIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }

    private fun createChannelIfNeeded(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(NotificationManager::class.java) ?: return
        if (manager.getNotificationChannel(CHANNEL_ID) != null) return
        manager.createNotificationChannel(
            NotificationChannel(CHANNEL_ID, "Daily Learning Reminders", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "A friendly daily nudge to keep learning and grow your streak"
            }
        )
    }
}
