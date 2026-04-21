package com.sree.smartexpensetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sree.smartexpensetracker.data.model.ExportResult
import com.sree.smartexpensetracker.data.repository.ExportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExportViewModel(
    private val repository: ExportRepository
) : ViewModel() {

    private val _exportResult = MutableStateFlow<ExportResult?>(null)
    val exportResult: StateFlow<ExportResult?> = _exportResult.asStateFlow()

    private val _isExporting = MutableStateFlow(false)
    val isExporting: StateFlow<Boolean> = _isExporting.asStateFlow()

    fun exportCsv() {
        viewModelScope.launch {
            _isExporting.value = true
            try {
                _exportResult.value = repository.exportTransactionsToCsv()
            } catch (e: Exception) {
                _exportResult.value = ExportResult(
                    success = false,
                    message = e.message ?: "CSV export failed",
                    filePath = null,
                    exportType = "CSV"
                )
            } finally {
                _isExporting.value = false
            }
        }
    }

    fun exportPdf() {
        viewModelScope.launch {
            _isExporting.value = true
            try {
                _exportResult.value = repository.exportTransactionsToPdf()
            } catch (e: Exception) {
                _exportResult.value = ExportResult(
                    success = false,
                    message = e.message ?: "PDF export failed",
                    filePath = null,
                    exportType = "PDF"
                )
            } finally {
                _isExporting.value = false
            }
        }
    }

    fun clearResult() {
        _exportResult.value = null
    }
}

class ExportViewModelFactory(
    private val repository: ExportRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExportViewModel::class.java)) {
            return ExportViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}