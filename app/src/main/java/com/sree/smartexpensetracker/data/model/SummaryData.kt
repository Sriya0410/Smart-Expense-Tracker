package com.sree.smartexpensetracker.data.model

data class SummaryData(
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val balance: Double = 0.0
) {
    val isPositiveBalance: Boolean
        get() = balance >= 0

    val expenseRatio: Float
        get() = if (totalIncome <= 0.0) 0f else (totalExpense / totalIncome).toFloat()

    val savingsRate: Float
        get() = if (totalIncome <= 0.0) 0f else (balance / totalIncome).toFloat()
}