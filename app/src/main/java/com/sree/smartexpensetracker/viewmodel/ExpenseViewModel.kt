package com.sree.smartexpensetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sree.smartexpensetracker.data.local.TransactionEntity
import com.sree.smartexpensetracker.data.local.TransactionType
import com.sree.smartexpensetracker.data.model.FilterState
import com.sree.smartexpensetracker.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExpenseViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    private val _filterState = MutableStateFlow(FilterState())
    val filterState: StateFlow<FilterState> = _filterState.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    private val allTransactions = repository.observeAllTransactions()

    val transactions: StateFlow<List<TransactionEntity>> =
        combine(allTransactions, _filterState) { list, filter ->
            list.filter { transaction ->
                val matchesQuery =
                    filter.searchQuery.isBlank() ||
                            transaction.title.contains(filter.searchQuery, ignoreCase = true) ||
                            transaction.category.contains(filter.searchQuery, ignoreCase = true) ||
                            transaction.note.contains(filter.searchQuery, ignoreCase = true)

                val matchesCategory =
                    filter.category.isNullOrBlank() ||
                            transaction.category.equals(filter.category, ignoreCase = true)

                val matchesType =
                    filter.type == null || transaction.type == filter.type

                matchesQuery && matchesCategory && matchesType
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val totalIncome: StateFlow<Double> =
        repository.observeTotalIncome()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = 0.0
            )

    val totalExpense: StateFlow<Double> =
        repository.observeTotalExpense()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = 0.0
            )

    fun updateSearchQuery(query: String) {
        _filterState.value = _filterState.value.copy(searchQuery = query)
    }

    fun updateCategory(category: String?) {
        _filterState.value = _filterState.value.copy(category = category)
    }

    fun updateType(type: TransactionType?) {
        _filterState.value = _filterState.value.copy(type = type)
    }

    fun clearFilters() {
        _filterState.value = FilterState()
    }

    fun addTransaction(
        title: String,
        amount: Double,
        category: String,
        note: String,
        type: TransactionType
    ) {
        if (title.isBlank()) {
            _message.value = "Title cannot be empty."
            return
        }

        if (amount <= 0.0) {
            _message.value = "Amount must be greater than zero."
            return
        }

        if (category.isBlank()) {
            _message.value = "Category cannot be empty."
            return
        }

        viewModelScope.launch {
            repository.insertTransaction(
                TransactionEntity(
                    title = title.trim(),
                    amount = amount,
                    category = category.trim(),
                    note = note.trim(),
                    type = type
                )
            )
            _message.value = "Transaction added successfully."
        }
    }

    fun updateTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.updateTransaction(transaction)
            _message.value = "Transaction updated successfully."
        }
    }

    fun deleteTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
            _message.value = "Transaction deleted successfully."
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}

class ExpenseViewModelFactory(
    private val repository: TransactionRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            return ExpenseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}