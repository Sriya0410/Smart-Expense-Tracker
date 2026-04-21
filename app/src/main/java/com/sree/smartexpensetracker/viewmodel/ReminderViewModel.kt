package com.sree.smartexpensetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sree.smartexpensetracker.data.local.ReminderEntity
import com.sree.smartexpensetracker.data.repository.ReminderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReminderViewModel(
    private val repository: ReminderRepository
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

        if (hour !in 0..23 || minute !in 0..59) {
            _message.value = "Enter a valid time."
            return
        }

        viewModelScope.launch {
            repository.insertReminder(
                ReminderEntity(
                    title = title.trim(),
                    message = message.trim(),
                    hour = hour,
                    minute = minute,
                    isEnabled = true
                )
            )
            _message.value = "Reminder added successfully."
        }
    }

    fun updateReminder(reminder: ReminderEntity) {
        viewModelScope.launch {
            repository.updateReminder(reminder)
            _message.value = "Reminder updated successfully."
        }
    }

    fun toggleReminder(reminder: ReminderEntity, enabled: Boolean) {
        viewModelScope.launch {
            repository.updateReminderStatus(reminder.id, enabled)
            _message.value = if (enabled) {
                "Reminder enabled."
            } else {
                "Reminder disabled."
            }
        }
    }

    fun deleteReminder(reminder: ReminderEntity) {
        viewModelScope.launch {
            repository.deleteReminder(reminder)
            _message.value = "Reminder deleted successfully."
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}

class ReminderViewModelFactory(
    private val repository: ReminderRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReminderViewModel::class.java)) {
            return ReminderViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}