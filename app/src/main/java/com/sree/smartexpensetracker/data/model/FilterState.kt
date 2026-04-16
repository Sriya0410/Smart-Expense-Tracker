package com.sree.smartexpensetracker.data.model

import com.sree.smartexpensetracker.data.local.TransactionType

data class FilterState(
    val searchQuery: String = "",
    val category: String? = null,
    val type: TransactionType? = null
) {
    val hasActiveFilters: Boolean
        get() = searchQuery.isNotBlank() || !category.isNullOrBlank() || type != null
}