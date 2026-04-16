package com.sree.smartexpensetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sree.smartexpensetracker.data.local.BudgetEntity
import com.sree.smartexpensetracker.data.repository.BudgetRepository
import com.sree.smartexpensetracker.utils.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BudgetViewModel(
    private val repository: BudgetRepository
) : ViewModel() {

    private val currentMonth = DateUtils.getCurrentMonth()
    private val currentYear = DateUtils.getCurrentYear()

    val budgets: StateFlow<List<BudgetEntity>> =
        repository.observeBudgetsForMonth(currentMonth, currentYear)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    fun addBudget(category: String, limitAmount: Double) {
        if (category.isBlank()) {
            _message.value = "Category cannot be empty."
            return
        }

        if (limitAmount <= 0.0) {
            _message.value = "Budget amount must be greater than zero."
            return
        }

        viewModelScope.launch {
            repository.upsertBudget(
                category = category.trim(),
                limitAmount = limitAmount,
                month = currentMonth,
                year = currentYear
            )
            _message.value = "Budget saved successfully."
        }
    }

    fun updateBudget(budget: BudgetEntity) {
        viewModelScope.launch {
            repository.updateBudget(budget)
            _message.value = "Budget updated successfully."
        }
    }

    fun deleteBudget(budget: BudgetEntity) {
        viewModelScope.launch {
            repository.deleteBudget(budget)
            _message.value = "Budget deleted successfully."
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}

class BudgetViewModelFactory(
    private val repository: BudgetRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BudgetViewModel::class.java)) {
            return BudgetViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}