package com.sree.smartexpensetracker.data.repository

import com.sree.smartexpensetracker.data.local.ReminderDao
import com.sree.smartexpensetracker.data.local.ReminderEntity
import kotlinx.coroutines.flow.Flow

class ReminderRepository(
    private val reminderDao: ReminderDao
) {

    fun observeAllReminders(): Flow<List<ReminderEntity>> {
        return reminderDao.observeAllReminders()
    }

    suspend fun getReminderById(id: Int): ReminderEntity? {
        return reminderDao.getReminderById(id)
    }

    suspend fun getEnabledReminders(): List<ReminderEntity> {
        return reminderDao.getEnabledReminders()
    }

    suspend fun insertReminder(reminder: ReminderEntity): Long {
        return reminderDao.insertReminder(reminder)
    }

    suspend fun updateReminder(reminder: ReminderEntity) {
        reminderDao.updateReminder(reminder)
    }

    suspend fun updateReminderStatus(reminderId: Int, enabled: Boolean) {
        reminderDao.updateReminderStatus(reminderId, enabled)
    }

    suspend fun deleteReminder(reminder: ReminderEntity) {
        reminderDao.deleteReminder(reminder)
    }
}