package com.sree.smartexpensetracker.data.repository

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.sree.smartexpensetracker.data.local.ReminderDao
import com.sree.smartexpensetracker.data.local.ReminderEntity
import com.sree.smartexpensetracker.worker.ExpenseReminderWorker
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import java.util.concurrent.TimeUnit

class ReminderRepository(
    private val reminderDao: ReminderDao,
    private val context: Context
) {

    fun observeAllReminders(): Flow<List<ReminderEntity>> {
        return reminderDao.observeAllReminders()
    }

    suspend fun insertReminder(reminder: ReminderEntity): Long {
        val reminderId = reminderDao.insertReminder(reminder)
        val savedReminder = reminder.copy(id = reminderId.toInt())

        if (savedReminder.isEnabled) {
            scheduleReminder(savedReminder)
        }

        return reminderId
    }

    suspend fun updateReminder(reminder: ReminderEntity) {
        reminderDao.updateReminder(reminder)
        cancelReminder(reminder.id)

        if (reminder.isEnabled) {
            scheduleReminder(reminder)
        }
    }

    suspend fun deleteReminder(reminder: ReminderEntity) {
        reminderDao.deleteReminder(reminder)
        cancelReminder(reminder.id)
    }

    suspend fun updateReminderStatus(reminderId: Int, enabled: Boolean) {
        reminderDao.updateReminderStatus(reminderId, enabled)

        val reminder = reminderDao.getReminderById(reminderId) ?: return

        if (enabled) {
            scheduleReminder(reminder.copy(isEnabled = true))
        } else {
            cancelReminder(reminderId)
        }
    }

    private fun scheduleReminder(reminder: ReminderEntity) {
        val currentTime = Calendar.getInstance()
        val reminderTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, reminder.hour)
            set(Calendar.MINUTE, reminder.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (before(currentTime)) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        val delay = reminderTime.timeInMillis - currentTime.timeInMillis

        val inputData = Data.Builder()
            .putInt("reminder_id", reminder.id)
            .putString("title", reminder.title)
            .putString("message", reminder.message)
            .putInt("hour", reminder.hour)
            .putInt("minute", reminder.minute)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<ExpenseReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "expense_reminder_${reminder.id}",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    private fun cancelReminder(reminderId: Int) {
        WorkManager.getInstance(context)
            .cancelUniqueWork("expense_reminder_$reminderId")
    }
}