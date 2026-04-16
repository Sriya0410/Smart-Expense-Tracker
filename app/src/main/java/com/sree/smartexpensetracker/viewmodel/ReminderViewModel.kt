package com.sree.smartexpensetracker.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.sree.smartexpensetracker.data.local.ReminderEntity
import com.sree.smartexpensetracker.data.repository.ReminderRepository
import com.sree.smartexpensetracker.worker.ExpenseReminderWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit

class ReminderViewModel(
    private val repository: ReminderRepository,
    private val context: Context
) : ViewModel() {

    val reminders: StateFlow<List<ReminderEntity>> =
        repository.observeAllReminders()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    fun addReminder(title: String, message: String, hour: Int, minute: Int) {
        if (title.isBlank()) {
            _message.value = "Reminder title cannot be empty."
            return
        }

        if (message.isBlank()) {
            _message.value = "Reminder message cannot be empty."
            return
        }

        viewModelScope.launch {
            val reminder = ReminderEntity(
                title = title.trim(),
                message = message.trim(),
                hour = hour,
                minute = minute,
                isEnabled = true
            )

            val reminderId = repository.insertReminder(reminder).toInt()

            scheduleReminder(
                reminderId = reminderId,
                title = title.trim(),
                message = message.trim(),
                hour = hour,
                minute = minute
            )

            _message.value = "Reminder added successfully."
        }
    }

    fun updateReminder(reminder: ReminderEntity) {
        viewModelScope.launch {
            repository.updateReminder(reminder)

            if (reminder.isEnabled) {
                scheduleReminder(
                    reminderId = reminder.id,
                    title = reminder.title,
                    message = reminder.message,
                    hour = reminder.hour,
                    minute = reminder.minute
                )
            } else {
                cancelReminder(reminder.id)
            }

            _message.value = "Reminder updated successfully."
        }
    }

    fun toggleReminder(reminder: ReminderEntity, enabled: Boolean) {
        viewModelScope.launch {
            repository.updateReminderStatus(reminder.id, enabled)

            if (enabled) {
                scheduleReminder(
                    reminderId = reminder.id,
                    title = reminder.title,
                    message = reminder.message,
                    hour = reminder.hour,
                    minute = reminder.minute
                )
            } else {
                cancelReminder(reminder.id)
            }
        }
    }

    fun deleteReminder(reminder: ReminderEntity) {
        viewModelScope.launch {
            repository.deleteReminder(reminder)
            cancelReminder(reminder.id)
            _message.value = "Reminder deleted successfully."
        }
    }

    fun clearMessage() {
        _message.value = null
    }

    private fun scheduleReminder(
        reminderId: Int,
        title: String,
        message: String,
        hour: Int,
        minute: Int
    ) {
        val currentTime = Calendar.getInstance()
        val reminderTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (before(currentTime)) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        val delay = reminderTime.timeInMillis - currentTime.timeInMillis

        val inputData = Data.Builder()
            .putInt("reminder_id", reminderId)
            .putString("title", title)
            .putString("message", message)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<ExpenseReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "expense_reminder_$reminderId",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    private fun cancelReminder(reminderId: Int) {
        WorkManager.getInstance(context).cancelUniqueWork("expense_reminder_$reminderId")
    }
}

class ReminderViewModelFactory(
    private val repository: ReminderRepository,
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReminderViewModel::class.java)) {
            return ReminderViewModel(repository, context.applicationContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}