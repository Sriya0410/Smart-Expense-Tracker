package com.sree.smartexpensetracker.data.repository

import com.sree.smartexpensetracker.data.local.BudgetDao
import com.sree.smartexpensetracker.data.local.BudgetEntity
import com.sree.smartexpensetracker.data.local.TransactionDao
import kotlinx.coroutines.flow.Flow

class BudgetRepository(
    private val budgetDao: BudgetDao,
    private val transactionDao: TransactionDao
) {

    fun observeAllBudgets(): Flow<List<BudgetEntity>> {
        return budgetDao.observeAllBudgets()
    }

    fun observeBudgetsForMonth(month: Int, year: Int): Flow<List<BudgetEntity>> {
        return budgetDao.observeBudgetsForMonth(month, year)
    }

    suspend fun insertBudget(budget: BudgetEntity) {
        budgetDao.insertBudget(budget)
    }

    suspend fun updateBudget(budget: BudgetEntity) {
        budgetDao.updateBudget(budget)
    }

    suspend fun deleteBudget(budget: BudgetEntity) {
        budgetDao.deleteBudget(budget)
    }

    suspend fun upsertBudget(
        category: String,
        limitAmount: Double,
        month: Int,
        year: Int
    ) {
        val existingBudgets = budgetDao.getBudgetsForMonthOnce(month, year)
        val existing = existingBudgets.firstOrNull {
            it.category.equals(category, ignoreCase = true)
        }

        if (existing != null) {
            budgetDao.updateBudget(
                existing.copy(limitAmount = limitAmount)
            )
        } else {
            budgetDao.insertBudget(
                BudgetEntity(
                    category = category,
                    limitAmount = limitAmount,
                    month = month,
                    year = year
                )
            )
        }
    }

    suspend fun getSpentAmountForCategory(
        category: String,
        startDate: Long,
        endDate: Long
    ): Double {
        return transactionDao.getExpenseTotalForCategory(category, startDate, endDate)
    }
}