package com.sree.smartexpensetracker.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "budgets",
    indices = [
        Index(value = ["category"]),
        Index(value = ["month", "year"]),
        Index(value = ["category", "month", "year"], unique = true)
    ]
)
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val category: String,
    val limitAmount: Double,
    val month: Int,
    val year: Int
)