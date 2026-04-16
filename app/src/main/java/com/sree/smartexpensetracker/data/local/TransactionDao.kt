package com.sree.smartexpensetracker.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

data class CategoryTotal(
    val category: String,
    val total: Double
)

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun observeAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    suspend fun getAllTransactions(): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE id = :id LIMIT 1")
    suspend fun getTransactionById(id: Int): TransactionEntity?

    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY timestamp DESC")
    fun observeTransactionsByType(type: TransactionType): Flow<List<TransactionEntity>>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'INCOME'")
    fun observeTotalIncome(): Flow<Double>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'EXPENSE'")
    fun observeTotalExpense(): Flow<Double>

    @Query("""
        SELECT * FROM transactions
        WHERE title LIKE '%' || :query || '%'
           OR category LIKE '%' || :query || '%'
           OR note LIKE '%' || :query || '%'
        ORDER BY timestamp DESC
    """)
    fun searchTransactions(query: String): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * FROM transactions
        WHERE (:category IS NULL OR :category = '' OR category = :category)
          AND (:type IS NULL OR type = :type)
        ORDER BY timestamp DESC
    """)
    fun filterTransactions(
        category: String?,
        type: TransactionType?
    ): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * FROM transactions
        WHERE (:query IS NULL OR :query = ''
            OR title LIKE '%' || :query || '%'
            OR category LIKE '%' || :query || '%'
            OR note LIKE '%' || :query || '%')
          AND (:category IS NULL OR :category = '' OR category = :category)
          AND (:type IS NULL OR type = :type)
        ORDER BY timestamp DESC
    """)
    fun searchAndFilterTransactions(
        query: String?,
        category: String?,
        type: TransactionType?
    ): Flow<List<TransactionEntity>>

    @Query("""
        SELECT category, COALESCE(SUM(amount), 0) AS total
        FROM transactions
        WHERE type = 'EXPENSE'
        GROUP BY category
        ORDER BY total DESC
    """)
    suspend fun getCategoryTotals(): List<CategoryTotal>

    @Query("""
        SELECT * FROM transactions
        WHERE timestamp BETWEEN :startDate AND :endDate
        ORDER BY timestamp DESC
    """)
    suspend fun getTransactionsBetween(startDate: Long, endDate: Long): List<TransactionEntity>

    @Query("""
        SELECT COALESCE(SUM(amount), 0)
        FROM transactions
        WHERE type = 'INCOME'
          AND timestamp BETWEEN :startDate AND :endDate
    """)
    suspend fun getIncomeBetween(startDate: Long, endDate: Long): Double

    @Query("""
        SELECT COALESCE(SUM(amount), 0)
        FROM transactions
        WHERE type = 'EXPENSE'
          AND timestamp BETWEEN :startDate AND :endDate
    """)
    suspend fun getExpenseBetween(startDate: Long, endDate: Long): Double

    @Query("""
        SELECT COALESCE(SUM(amount), 0)
        FROM transactions
        WHERE type = 'EXPENSE'
          AND category = :category
          AND timestamp BETWEEN :startDate AND :endDate
    """)
    suspend fun getExpenseTotalForCategory(
        category: String,
        startDate: Long,
        endDate: Long
    ): Double

    @Query("""
        SELECT DISTINCT category
        FROM transactions
        ORDER BY category ASC
    """)
    fun observeAllCategories(): Flow<List<String>>
}