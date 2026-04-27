package com.sree.smartexpensetracker.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.sree.smartexpensetracker.ui.components.AppDrawerContent
import com.sree.smartexpensetracker.ui.screens.*
import com.sree.smartexpensetracker.viewmodel.*
import kotlinx.coroutines.launch

@Composable
fun AppNavGraph(
    navController: NavHostController,
    expenseViewModel: ExpenseViewModel,
    budgetViewModel: BudgetViewModel,
    analyticsViewModel: AnalyticsViewModel,
    insightViewModel: InsightViewModel,
    exportViewModel: ExportViewModel,
    modifier: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val drawerRoutes = setOf(
        Screen.Dashboard.route,
        Screen.TransactionHistory.route,
        Screen.Budget.route,
        Screen.Analytics.route,
        Screen.Insights.route,
        Screen.Export.route,
        Screen.AddTransaction.route
    )

    val showDrawer = currentRoute in drawerRoutes

    BackHandler(enabled = drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = showDrawer,
        drawerContent = {
            AppDrawerContent(
                currentRoute = currentRoute,
                onItemClick = { route ->
                    scope.launch { drawerState.close() }
                    if (currentRoute != route) {
                        navController.navigate(route) {
                            popUpTo(Screen.Dashboard.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Landing.route,
            modifier = modifier
        ) {

            composable(Screen.Landing.route) {
                LandingScreen {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Landing.route) { inclusive = true }
                    }
                }
            }

            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    expenseViewModel,
                    analyticsViewModel,
                    insightViewModel,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onAddTransactionClick = {
                        navController.navigate(Screen.AddTransaction.route)
                    },
                    onHistoryClick = {
                        navController.navigate(Screen.TransactionHistory.route)
                    },
                    onBudgetClick = {
                        navController.navigate(Screen.Budget.route)
                    },
                    onAnalyticsClick = {
                        navController.navigate(Screen.Analytics.route)
                    }
                )
            }

            composable(Screen.AddTransaction.route) {
                AddTransactionScreen(
                    expenseViewModel,
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }

            composable(Screen.TransactionHistory.route) {
                TransactionHistoryScreen(
                    expenseViewModel,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onBackClick = {}
                )
            }

            composable(Screen.Budget.route) {
                BudgetScreen(
                    budgetViewModel,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onBackClick = {}
                )
            }

            composable(Screen.Analytics.route) {
                AnalyticsScreen(
                    analyticsViewModel,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onBackClick = {}
                )
            }

            composable(Screen.Insights.route) {
                InsightsScreen(
                    insightViewModel,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onBackClick = {}
                )
            }

            composable(Screen.Export.route) {
                ExportScreen(
                    exportViewModel,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onBackClick = {}
                )
            }
        }
    }
}