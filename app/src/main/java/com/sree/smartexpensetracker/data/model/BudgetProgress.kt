package com.sree.smartexpensetracker.data.model

import kotlin.math.roundToInt

data class BudgetProgress(
    val category: String,
    val limitAmount: Double,
    val spentAmount: Double,
    val remainingAmount: Double,
    val progress: Float
) {
    val safeProgress: Float
        get() = progress.coerceIn(0f, 1f)

    val isOverBudget: Boolean
        get() = spentAmount > limitAmount

    val usagePercentage: Int
        get() = (safeProgress * 100).roundToInt()
}