package com.sree.smartexpensetracker.data.repository

import com.sree.smartexpensetracker.data.local.TransactionDao
import com.sree.smartexpensetracker.data.local.TransactionEntity
import com.sree.smartexpensetracker.data.local.TransactionType
import kotlinx.coroutines.flow.Flow

class TransactionRepository(
    private val transactionDao: TransactionDao
) {

    fun observeAllTransactions(): Flow<List<TransactionEntity>> {
        return transactionDao.observeAllTransactions()
    }

    fun observeTransactionsByType(type: TransactionType): Flow<List<TransactionEntity>> {
        return transactionDao.observeTransactionsByType(type)
    }

    fun observeTotalIncome(): Flow<Double> {
        return transactionDao.observeTotalIncome()
    }

    fun observeTotalExpense(): Flow<Double> {
        return transactionDao.observeTotalExpense()
    }

    fun observeAllCategories(): Flow<List<String>> {
        return transactionDao.observeAllCategories()
    }

    fun searchTransactions(query: String): Flow<List<TransactionEntity>> {
        return transactionDao.searchTransactions(query)
    }

    fun filterTransactions(
        category: String?,
        type: TransactionType?
    ): Flow<List<TransactionEntity>> {
        return transactionDao.filterTransactions(category, type)
    }

    fun searchAndFilterTransactions(
        query: String?,
        category: String?,
        type: TransactionType?
    ): Flow<List<TransactionEntity>> {
        return transactionDao.searchAndFilterTransactions(query, category, type)
    }

    suspend fun insertTransaction(transaction: TransactionEntity): Long {
        return transactionDao.insertTransaction(transaction)
    }

    suspend fun updateTransaction(transaction: TransactionEntity) {
        transactionDao.updateTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction: TransactionEntity) {
        transactionDao.deleteTransaction(transaction)
    }

    suspend fun getTransactionById(id: Int): TransactionEntity? {
        return transactionDao.getTransactionById(id)
    }

    suspend fun getAllTransactions(): List<TransactionEntity> {
        return transactionDao.getAllTransactions()
    }

    suspend fun getTransactionsBetween(startDate: Long, endDate: Long): List<TransactionEntity> {
        return transactionDao.getTransactionsBetween(startDate, endDate)
    }

    suspend fun getIncomeBetween(startDate: Long, endDate: Long): Double {
        return transactionDao.getIncomeBetween(startDate, endDate)
    }

    suspend fun getExpenseBetween(startDate: Long, endDate: Long): Double {
        return transactionDao.getExpenseBetween(startDate, endDate)
    }

    suspend fun getExpenseTotalForCategory(
        category: String,
        startDate: Long,
        endDate: Long
    ): Double {
        return transactionDao.getExpenseTotalForCategory(category, startDate, endDate)
    }
}