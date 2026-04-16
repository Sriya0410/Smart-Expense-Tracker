package com.sree.smartexpensetracker.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.sree.smartexpensetracker.data.local.TransactionEntity
import com.sree.smartexpensetracker.data.model.ExportResult
import java.io.OutputStreamWriter

object CsvExporter {

    fun exportTransactions(
        context: Context,
        transactions: List<TransactionEntity>
    ): ExportResult {
        var uri: Uri? = null

        return try {
            val fileName = "SmartExpense_${System.currentTimeMillis()}.csv"

            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }

            uri = context.contentResolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                values
            ) ?: return ExportResult(
                success = false,
                message = "Failed to create CSV file in Downloads"
            )

            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                OutputStreamWriter(outputStream).use { writer ->
                    writer.appendLine("Id,Title,Amount,Category,Note,Type,Date")

                    transactions.forEach { transaction ->
                        val safeTitle = transaction.title.replace(",", " ")
                        val safeCategory = transaction.category.replace(",", " ")
                        val safeNote = transaction.note.replace(",", " ")
                        val safeDate = DateUtils.formatDate(transaction.timestamp)

                        writer.appendLine(
                            "${transaction.id}," +
                                    "$safeTitle," +
                                    "${transaction.amount}," +
                                    "$safeCategory," +
                                    "$safeNote," +
                                    "${transaction.type}," +
                                    "$safeDate"
                        )
                    }

                    writer.flush()
                }
            } ?: return ExportResult(
                success = false,
                message = "Unable to open CSV output stream"
            )

            val completedValues = ContentValues().apply {
                put(MediaStore.MediaColumns.IS_PENDING, 0)
            }
            context.contentResolver.update(uri, completedValues, null, null)

            ExportResult(
                success = true,
                message = "CSV saved to Downloads",
                filePath = uri.toString()
            )
        } catch (e: Exception) {
            uri?.let { context.contentResolver.delete(it, null, null) }
            ExportResult(
                success = false,
                message = e.message ?: "CSV export failed"
            )
        }
    }
}