package com.sree.smartexpensetracker.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: BudgetEntity): Long

    @Update
    suspend fun updateBudget(budget: BudgetEntity)

    @Delete
    suspend fun deleteBudget(budget: BudgetEntity)

    @Query("SELECT * FROM budgets ORDER BY year DESC, month DESC, category ASC")
    fun observeAllBudgets(): Flow<List<BudgetEntity>>

    @Query("""
        SELECT * FROM budgets
        WHERE month = :month AND year = :year
        ORDER BY category ASC
    """)
    fun observeBudgetsForMonth(month: Int, year: Int): Flow<List<BudgetEntity>>

    @Query("""
        SELECT * FROM budgets
        WHERE month = :month AND year = :year AND category = :category
        LIMIT 1
    """)
    suspend fun getBudgetByCategoryAndMonth(
        category: String,
        month: Int,
        year: Int
    ): BudgetEntity?

    @Query("SELECT * FROM budgets WHERE id = :id LIMIT 1")
    suspend fun getBudgetById(id: Int): BudgetEntity?
}