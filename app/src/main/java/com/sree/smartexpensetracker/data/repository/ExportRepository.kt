package com.sree.smartexpensetracker.data.repository

import android.content.Context
import com.sree.smartexpensetracker.data.model.ExportResult
import com.sree.smartexpensetracker.utils.CsvExporter
import com.sree.smartexpensetracker.utils.PdfExporter

class ExportRepository(
    private val context: Context,
    private val transactionRepository: TransactionRepository
) {

    suspend fun exportTransactionsToCsv(): ExportResult {
        val transactions = transactionRepository.getAllTransactions()
        return CsvExporter.exportTransactions(context, transactions)
            .copy(exportType = "CSV")
    }

    suspend fun exportTransactionsToPdf(): ExportResult {
        val transactions = transactionRepository.getAllTransactions()
        return PdfExporter.exportTransactions(context, transactions)
            .copy(exportType = "PDF")
    }
}