package com.sree.smartexpensetracker.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reminders",
    indices = [
        Index(value = ["hour", "minute"]),
        Index(value = ["isEnabled"])
    ]
)
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val message: String,
    val hour: Int,
    val minute: Int,
    val isEnabled: Boolean = true
)