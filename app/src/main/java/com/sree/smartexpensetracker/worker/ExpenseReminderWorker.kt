package com.sree.smartexpensetracker.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.sree.smartexpensetracker.utils.NotificationUtils
import java.util.concurrent.TimeUnit

class ExpenseReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val reminderId = inputData.getInt("reminder_id", -1)
            val title = inputData.getString("title") ?: "Expense Reminder"
            val message = inputData.getString("message")
                ?: "Don't forget to track your expenses today."
            val hour = inputData.getInt("hour", 9)
            val minute = inputData.getInt("minute", 0)

            NotificationUtils.showNotification(applicationContext, title, message)

            if (reminderId != -1) {
                val nextInputData = Data.Builder()
                    .putInt("reminder_id", reminderId)
                    .putString("title", title)
                    .putString("message", message)
                    .putInt("hour", hour)
                    .putInt("minute", minute)
                    .build()

                val nextWorkRequest = OneTimeWorkRequestBuilder<ExpenseReminderWorker>()
                    .setInitialDelay(24, TimeUnit.HOURS)
                    .setInputData(nextInputData)
                    .build()

                WorkManager.getInstance(applicationContext).enqueueUniqueWork(
                    "expense_reminder_$reminderId",
                    ExistingWorkPolicy.REPLACE,
                    nextWorkRequest
                )
            }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}