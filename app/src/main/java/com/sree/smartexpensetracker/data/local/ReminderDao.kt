package com.sree.smartexpensetracker.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderEntity): Long

    @Update
    suspend fun updateReminder(reminder: ReminderEntity)

    @Delete
    suspend fun deleteReminder(reminder: ReminderEntity)

    @Query("SELECT * FROM reminders ORDER BY hour ASC, minute ASC")
    fun observeAllReminders(): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE id = :id LIMIT 1")
    suspend fun getReminderById(id: Int): ReminderEntity?

    @Query("SELECT * FROM reminders WHERE isEnabled = 1 ORDER BY hour ASC, minute ASC")
    suspend fun getEnabledReminders(): List<ReminderEntity>

    @Query("UPDATE reminders SET isEnabled = :enabled WHERE id = :reminderId")
    suspend fun updateReminderStatus(reminderId: Int, enabled: Boolean)
}