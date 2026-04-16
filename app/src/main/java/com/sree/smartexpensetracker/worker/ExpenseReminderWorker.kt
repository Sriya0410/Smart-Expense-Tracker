package com.sree.smartexpensetracker.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sree.smartexpensetracker.utils.NotificationUtils

class ExpenseReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val title = inputData.getString("title") ?: "Expense Reminder"
            val message = inputData.getString("message")
                ?: "Don't forget to track your expenses today."

            NotificationUtils.showNotification(applicationContext, title, message)

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}