package com.sree.smartexpensetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sree.smartexpensetracker.data.model.InsightItem
import com.sree.smartexpensetracker.data.repository.InsightRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InsightViewModel(
    private val repository: InsightRepository
) : ViewModel() {

    private val _insights = MutableStateFlow<List<InsightItem>>(emptyList())
    val insights: StateFlow<List<InsightItem>> = _insights.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        observeTransactions()
    }

    private fun observeTransactions() {
        viewModelScope.launch {
            repository.observeTransactionRefreshKey().collect {
                loadInsights()
            }
        }
    }

    fun loadInsights() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _insights.value = repository.generateInsights()
            } finally {
                _isLoading.value = false
            }
        }
    }
}

class InsightViewModelFactory(
    private val repository: InsightRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InsightViewModel::class.java)) {
            return InsightViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}