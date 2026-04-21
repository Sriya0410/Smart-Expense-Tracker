package com.sree.smartexpensetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sree.smartexpensetracker.data.model.CategoryExpense
import com.sree.smartexpensetracker.data.model.SummaryData
import com.sree.smartexpensetracker.data.repository.AnalyticsRepository
import com.sree.smartexpensetracker.utils.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnalyticsViewModel(
    private val repository: AnalyticsRepository
) : ViewModel() {

    private val _summary = MutableStateFlow(SummaryData())
    val summary: StateFlow<SummaryData> = _summary.asStateFlow()

    private val _categoryExpenses = MutableStateFlow<List<CategoryExpense>>(emptyList())
    val categoryExpenses: StateFlow<List<CategoryExpense>> = _categoryExpenses.asStateFlow()

    private val _monthlyExpenseTotal = MutableStateFlow(0.0)
    val monthlyExpenseTotal: StateFlow<Double> = _monthlyExpenseTotal.asStateFlow()

    private val _weeklyExpenseTotal = MutableStateFlow(0.0)
    val weeklyExpenseTotal: StateFlow<Double> = _weeklyExpenseTotal.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        observeSummary()
        observeTransactions()
    }

    private fun observeSummary() {
        viewModelScope.launch {
            repository.observeSummary().collect { summaryData ->
                _summary.value = summaryData
            }
        }
    }

    private fun observeTransactions() {
        viewModelScope.launch {
            repository.observeTransactionRefreshKey().collect {
                refreshAnalytics()
            }
        }
    }

    fun refreshAnalytics() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                loadCategoryExpenses()
                loadMonthlyExpenses()
                loadWeeklyExpenses()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun loadCategoryExpenses() {
        _categoryExpenses.value = repository.getCategoryExpenses()
    }

    private suspend fun loadMonthlyExpenses() {
        val start = DateUtils.getStartOfCurrentMonth()
        val end = DateUtils.getEndOfCurrentMonth()
        _monthlyExpenseTotal.value = repository.getExpenseBetween(start, end)
    }

    private suspend fun loadWeeklyExpenses() {
        val start = DateUtils.getStartOfCurrentWeek()
        val end = DateUtils.getEndOfCurrentWeek()
        _weeklyExpenseTotal.value = repository.getExpenseBetween(start, end)
    }
}

class AnalyticsViewModelFactory(
    private val repository: AnalyticsRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnalyticsViewModel::class.java)) {
            return AnalyticsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}