package com.sree.smartexpensetracker.navigation

sealed class Screen(val route: String, val title: String) {

    object Landing : Screen("landing", "Welcome")

    object Dashboard : Screen("dashboard", "Dashboard")
    object AddTransaction : Screen("add_transaction", "Add Transaction")
    object TransactionHistory : Screen("transaction_history", "Transaction History")
    object Budget : Screen("budget", "Budget Management")
    object Analytics : Screen("analytics", "Analytics")
    object Insights : Screen("insights", "Smart Insights")
    object Export : Screen("export", "Export Report")
}