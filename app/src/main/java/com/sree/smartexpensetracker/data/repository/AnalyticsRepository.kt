package com.sree.smartexpensetracker.data.repository

import com.sree.smartexpensetracker.data.local.TransactionDao
import com.sree.smartexpensetracker.data.local.TransactionEntity
import com.sree.smartexpensetracker.data.local.TransactionType
import com.sree.smartexpensetracker.data.model.CategoryExpense
import com.sree.smartexpensetracker.data.model.SummaryData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class AnalyticsRepository(
    private val transactionDao: TransactionDao
) {

    fun observeSummary(): Flow<SummaryData> {
        return combine(
            transactionDao.observeTotalIncome(),
            transactionDao.observeTotalExpense()
        ) { income, expense ->
            SummaryData(
                totalIncome = income,
                totalExpense = expense,
                balance = income - expense
            )
        }
    }

    fun observeTransactionRefreshKey(): Flow<Int> {
        return transactionDao.observeAllTransactions().map { it.size }
    }

    suspend fun getCategoryExpenses(): List<CategoryExpense> {
        return transactionDao.getCategoryTotals().map {
            CategoryExpense(
                category = it.category,
                amount = it.total
            )
        }
    }

    suspend fun getExpensesBetween(startDate: Long, endDate: Long): List<TransactionEntity> {
        return transactionDao.getTransactionsBetween(startDate, endDate)
            .filter { it.type == TransactionType.EXPENSE }
    }

    suspend fun getIncomeBetween(startDate: Long, endDate: Long): Double {
        return transactionDao.getIncomeBetween(startDate, endDate)
    }

    suspend fun getExpenseBetween(startDate: Long, endDate: Long): Double {
        return transactionDao.getExpenseBetween(startDate, endDate)
    }

    suspend fun getMonthlyExpenses(startDate: Long, endDate: Long): List<TransactionEntity> {
        return transactionDao.getTransactionsBetween(startDate, endDate)
            .filter { it.type == TransactionType.EXPENSE }
    }

    suspend fun getWeeklyExpenses(startDate: Long, endDate: Long): List<TransactionEntity> {
        return transactionDao.getTransactionsBetween(startDate, endDate)
            .filter { it.type == TransactionType.EXPENSE }
    }
}