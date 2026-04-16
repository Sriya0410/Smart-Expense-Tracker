package com.sree.smartexpensetracker.utils

import java.text.NumberFormat
import java.util.Locale

object CurrencyUtils {

    private val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))

    fun format(amount: Double): String {
        return try {
            formatter.format(amount)
        } catch (e: Exception) {
            "₹0"
        }
    }

    fun formatShort(amount: Double): String {
        return when {
            amount >= 1_000_000 -> "₹${"%.1f".format(amount / 1_000_000)}M"
            amount >= 1_000 -> "₹${"%.1f".format(amount / 1_000)}K"
            else -> format(amount)
        }
    }
}