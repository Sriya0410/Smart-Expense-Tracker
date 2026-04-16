package com.sree.smartexpensetracker.data.model

data class ExportResult(
    val success: Boolean,
    val message: String,
    val filePath: String? = null,
    val exportType: String? = null,
    val exportedAt: Long = System.currentTimeMillis()
)