package com.sree.smartexpensetracker.data.repository

import com.sree.smartexpensetracker.data.local.BudgetDao
import com.sree.smartexpensetracker.data.local.BudgetEntity
import kotlinx.coroutines.flow.Flow

class BudgetRepository(
    private val budgetDao: BudgetDao
) {

    fun observeAllBudgets(): Flow<List<BudgetEntity>> {
        return budgetDao.observeAllBudgets()
    }

    fun observeBudgetsForMonth(month: Int, year: Int): Flow<List<BudgetEntity>> {
        return budgetDao.observeBudgetsForMonth(month, year)
    }

    suspend fun getBudgetById(id: Int): BudgetEntity? {
        return budgetDao.getBudgetById(id)
    }

    suspend fun getBudgetByCategoryAndMonth(
        category: String,
        month: Int,
        year: Int
    ): BudgetEntity? {
        return budgetDao.getBudgetByCategoryAndMonth(category, month, year)
    }

    suspend fun insertBudget(budget: BudgetEntity): Long {
        return budgetDao.insertBudget(budget)
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
        val existing = budgetDao.getBudgetByCategoryAndMonth(category, month, year)

        if (existing == null) {
            budgetDao.insertBudget(
                BudgetEntity(
                    category = category,
                    limitAmount = limitAmount,
                    month = month,
                    year = year
                )
            )
        } else {
            budgetDao.updateBudget(
                existing.copy(limitAmount = limitAmount)
            )
        }
    }
}