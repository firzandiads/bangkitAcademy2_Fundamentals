package com.example.submissionawal.util
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.submissionawal.R


class Reminders(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val eventName = inputData.getString("event_name")
        val eventTime = inputData.getString("event_time")

        if (!eventName.isNullOrEmpty() && !eventTime.isNullOrEmpty()) {
            showNotification(eventName, eventTime)
            return Result.success()
        }
        return Result.failure()
    }



    private fun showNotification(eventName: String, eventTime: String) {
        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notification: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notif)
                .setContentTitle(eventName)
                .setContentText(eventTime)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }



    companion object {
        const val CHANNEL_ID = "daily_reminder_channel"
        const val CHANNEL_NAME = "Daily Reminder"
        const val NOTIFICATION_ID = 1
    }
}