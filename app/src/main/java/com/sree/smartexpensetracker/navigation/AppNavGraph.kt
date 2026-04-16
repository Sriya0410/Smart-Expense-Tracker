package com.sree.smartexpensetracker.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sree.smartexpensetracker.ui.components.AppDrawerContent
import com.sree.smartexpensetracker.ui.screens.AddTransactionScreen
import com.sree.smartexpensetracker.ui.screens.AnalyticsScreen
import com.sree.smartexpensetracker.ui.screens.BudgetScreen
import com.sree.smartexpensetracker.ui.screens.DashboardScreen
import com.sree.smartexpensetracker.ui.screens.ExportScreen
import com.sree.smartexpensetracker.ui.screens.InsightsScreen
import com.sree.smartexpensetracker.ui.screens.LandingScreen
import com.sree.smartexpensetracker.ui.screens.ReminderScreen
import com.sree.smartexpensetracker.ui.screens.TransactionHistoryScreen
import com.sree.smartexpensetracker.viewmodel.AnalyticsViewModel
import com.sree.smartexpensetracker.viewmodel.BudgetViewModel
import com.sree.smartexpensetracker.viewmodel.ExpenseViewModel
import com.sree.smartexpensetracker.viewmodel.ExportViewModel
import com.sree.smartexpensetracker.viewmodel.InsightViewModel
import com.sree.smartexpensetracker.viewmodel.ReminderViewModel
import kotlinx.coroutines.launch

@Composable
fun AppNavGraph(
    navController: NavHostController,
    expenseViewModel: ExpenseViewModel,
    budgetViewModel: BudgetViewModel,
    analyticsViewModel: AnalyticsViewModel,
    insightViewModel: InsightViewModel,
    reminderViewModel: ReminderViewModel,
    exportViewModel: ExportViewModel,
    modifier: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val drawerRoutes = setOf(
        Screen.Dashboard.route,
        Screen.TransactionHistory.route,
        Screen.Budget.route,
        Screen.Analytics.route,
        Screen.Insights.route,
        Screen.Reminder.route,
        Screen.Export.route,
        Screen.AddTransaction.route
    )

    val showDrawer = currentRoute in drawerRoutes

    LaunchedEffect(currentRoute) {
        if (!showDrawer && drawerState.isOpen) {
            drawerState.close()
        }
    }

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
                            popUpTo(Screen.Dashboard.route) {
                                saveState = true
                            }
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
                LandingScreen(
                    onGetStarted = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Landing.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    expenseViewModel = expenseViewModel,
                    analyticsViewModel = analyticsViewModel,
                    insightViewModel = insightViewModel,
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
                    expenseViewModel = expenseViewModel,
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }

            composable(Screen.TransactionHistory.route) {
                TransactionHistoryScreen(
                    expenseViewModel = expenseViewModel,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onBackClick = {}
                )
            }

            composable(Screen.Budget.route) {
                BudgetScreen(
                    budgetViewModel = budgetViewModel,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onBackClick = {}
                )
            }

            composable(Screen.Analytics.route) {
                AnalyticsScreen(
                    analyticsViewModel = analyticsViewModel,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onBackClick = {}
                )
            }

            composable(Screen.Insights.route) {
                InsightsScreen(
                    insightViewModel = insightViewModel,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onBackClick = {}
                )
            }

            composable(Screen.Reminder.route) {
                ReminderScreen(
                    reminderViewModel = reminderViewModel,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onBackClick = {}
                )
            }

            composable(Screen.Export.route) {
                ExportScreen(
                    exportViewModel = exportViewModel,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onBackClick = {}
                )
            }
        }
    }
}