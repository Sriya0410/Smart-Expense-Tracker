package com.sree.smartexpensetracker.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.sree.smartexpensetracker.data.local.TransactionEntity
import com.sree.smartexpensetracker.data.model.ExportResult

object PdfExporter {

    fun exportTransactions(
        context: Context,
        transactions: List<TransactionEntity>
    ): ExportResult {
        var uri: Uri? = null
        val document = PdfDocument()

        return try {
            val titlePaint = Paint().apply {
                textSize = 20f
                isFakeBoldText = true
            }

            val textPaint = Paint().apply {
                textSize = 12f
            }

            var pageNumber = 1
            var pageInfo = PdfDocument.PageInfo.Builder(1200, 1800, pageNumber).create()
            var page = document.startPage(pageInfo)
            var canvas = page.canvas
            var y = 60

            canvas.drawText("Smart Expense Tracker Report", 40f, y.toFloat(), titlePaint)
            y += 40
            canvas.drawText(
                "Generated on: ${DateUtils.formatDate(System.currentTimeMillis())}",
                40f,
                y.toFloat(),
                textPaint
            )
            y += 40

            transactions.forEachIndexed { index, transaction ->
                if (y > 1700) {
                    document.finishPage(page)
                    pageNumber++
                    pageInfo = PdfDocument.PageInfo.Builder(1200, 1800, pageNumber).create()
                    page = document.startPage(pageInfo)
                    canvas = page.canvas
                    y = 60
                }

                val line = "${index + 1}. ${transaction.title} | ₹${transaction.amount} | ${transaction.category} | ${transaction.type} | ${DateUtils.formatDate(transaction.timestamp)}"
                canvas.drawText(line, 40f, y.toFloat(), textPaint)
                y += 25
            }

            document.finishPage(page)

            val fileName = "SmartExpense_${System.currentTimeMillis()}.pdf"

            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }

            uri = context.contentResolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                values
            ) ?: return ExportResult(
                success = false,
                message = "Failed to create PDF file in Downloads"
            )

            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                document.writeTo(outputStream)
            } ?: return ExportResult(
                success = false,
                message = "Unable to open PDF output stream"
            )

            val completedValues = ContentValues().apply {
                put(MediaStore.MediaColumns.IS_PENDING, 0)
            }
            context.contentResolver.update(uri, completedValues, null, null)

            ExportResult(
                success = true,
                message = "PDF saved to Downloads",
                filePath = uri.toString()
            )
        } catch (e: Exception) {
            uri?.let { context.contentResolver.delete(it, null, null) }
            ExportResult(
                success = false,
                message = e.message ?: "PDF export failed"
            )
        } finally {
            document.close()
        }
    }
}