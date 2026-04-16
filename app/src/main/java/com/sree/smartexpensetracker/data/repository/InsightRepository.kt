package com.sree.smartexpensetracker.data.repository

import com.sree.smartexpensetracker.data.local.TransactionDao
import com.sree.smartexpensetracker.data.local.TransactionType
import com.sree.smartexpensetracker.data.model.InsightItem
import com.sree.smartexpensetracker.data.model.InsightSeverity
import kotlin.math.abs

class InsightRepository(
    private val transactionDao: TransactionDao
) {

    suspend fun generateInsights(): List<InsightItem> {
        val allTransactions = transactionDao.getAllTransactions()
        val expenses = allTransactions.filter { it.type == TransactionType.EXPENSE }
        val incomes = allTransactions.filter { it.type == TransactionType.INCOME }

        val insights = mutableListOf<InsightItem>()

        val totalExpense = expenses.sumOf { it.amount }
        val totalIncome = incomes.sumOf { it.amount }
        val savings = totalIncome - totalExpense

        if (expenses.isNotEmpty()) {
            val topCategory = expenses
                .groupBy { it.category }
                .mapValues { (_, items) -> items.sumOf { it.amount } }
                .maxByOrNull { it.value }

            if (topCategory != null) {
                insights.add(
                    InsightItem(
                        title = "Top Spending Category",
                        description = "You spent the most on ${topCategory.key}.",
                        recommendation = "Review this category and see where you can reduce unnecessary spending.",
                        severity = InsightSeverity.WARNING,
                        highlightValue = "₹${"%.2f".format(topCategory.value)}"
                    )
                )
            }
        }

        if (totalIncome > 0 && totalExpense > 0) {
            insights.add(
                InsightItem(
                    title = "Monthly Savings Snapshot",
                    description = if (savings >= 0) {
                        "Your current savings are positive this cycle."
                    } else {
                        "Your expenses are currently higher than your income."
                    },
                    recommendation = if (savings >= 0) {
                        "Nice work. Try to maintain this savings pattern consistently."
                    } else {
                        "Reduce discretionary spending and review large recent expense entries."
                    },
                    severity = if (savings >= 0) InsightSeverity.POSITIVE else InsightSeverity.WARNING,
                    highlightValue = "₹${"%.2f".format(abs(savings))}"
                )
            )
        }

        if (expenses.size >= 5) {
            val avgExpense = expenses.map { it.amount }.average()
            val highExpense = expenses.maxByOrNull { it.amount }

            if (highExpense != null && highExpense.amount > avgExpense * 1.5) {
                insights.add(
                    InsightItem(
                        title = "Unusual Spending Alert",
                        description = "A significantly high expense was recorded under ${highExpense.category}.",
                        recommendation = "Check whether this was a one-time necessary expense or part of a growing pattern.",
                        severity = InsightSeverity.WARNING,
                        highlightValue = "₹${"%.2f".format(highExpense.amount)}"
                    )
                )
            }
        }

        if (totalIncome > 0) {
            val expenseRatio = totalExpense / totalIncome

            when {
                expenseRatio < 0.6 -> {
                    insights.add(
                        InsightItem(
                            title = "Healthy Spending Pattern",
                            description = "Your expense-to-income ratio looks well controlled.",
                            recommendation = "Keep following this pattern and consider allocating more toward savings or investment goals.",
                            severity = InsightSeverity.POSITIVE,
                            highlightValue = "${(expenseRatio * 100).toInt()}%"
                        )
                    )
                }

                expenseRatio in 0.6..0.9 -> {
                    insights.add(
                        InsightItem(
                            title = "Balanced Financial Activity",
                            description = "Your spending is manageable, but there is still room for optimization.",
                            recommendation = "Track your frequent categories and cut back on avoidable expenses.",
                            severity = InsightSeverity.INFO,
                            highlightValue = "${(expenseRatio * 100).toInt()}%"
                        )
                    )
                }

                expenseRatio > 0.9 -> {
                    insights.add(
                        InsightItem(
                            title = "High Expense Ratio",
                            description = "A large portion of your income is being used for expenses.",
                            recommendation = "Set stricter monthly category budgets and review your top three spending areas.",
                            severity = InsightSeverity.WARNING,
                            highlightValue = "${(expenseRatio * 100).toInt()}%"
                        )
                    )
                }
            }
        }

        if (incomes.isNotEmpty() && expenses.isEmpty()) {
            insights.add(
                InsightItem(
                    title = "Strong Start",
                    description = "You have started logging income but no expenses yet.",
                    recommendation = "Add your daily expenses to unlock deeper analytics and more personalized insights.",
                    severity = InsightSeverity.INFO
                )
            )
        }

        if (allTransactions.isEmpty()) {
            insights.add(
                InsightItem(
                    title = "No Insights Yet",
                    description = "Add transactions to unlock smart insights, budget tracking, and analytics.",
                    recommendation = "Start by adding your first income or expense transaction.",
                    severity = InsightSeverity.INFO
                )
            )
        }

        return insights.distinctBy { it.title }
    }
}