package com.sree.smartexpensetracker.data.model

data class InsightItem(
    val title: String,
    val description: String,
    val recommendation: String = "",
    val severity: InsightSeverity = InsightSeverity.INFO,
    val highlightValue: String = ""
)

enum class InsightSeverity {
    INFO,
    WARNING,
    POSITIVE
}