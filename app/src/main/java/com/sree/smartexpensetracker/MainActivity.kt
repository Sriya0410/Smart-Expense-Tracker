package com.sree.smartexpensetracker

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.sree.smartexpensetracker.data.local.ExpenseDatabase
import com.sree.smartexpensetracker.data.repository.AnalyticsRepository
import com.sree.smartexpensetracker.data.repository.BudgetRepository
import com.sree.smartexpensetracker.data.repository.ExportRepository
import com.sree.smartexpensetracker.data.repository.InsightRepository
import com.sree.smartexpensetracker.data.repository.ReminderRepository
import com.sree.smartexpensetracker.data.repository.TransactionRepository
import com.sree.smartexpensetracker.navigation.AppNavGraph
import com.sree.smartexpensetracker.ui.theme.SmartExpenseTrackerTheme
import com.sree.smartexpensetracker.utils.NotificationUtils
import com.sree.smartexpensetracker.viewmodel.AnalyticsViewModel
import com.sree.smartexpensetracker.viewmodel.AnalyticsViewModelFactory
import com.sree.smartexpensetracker.viewmodel.BudgetViewModel
import com.sree.smartexpensetracker.viewmodel.BudgetViewModelFactory
import com.sree.smartexpensetracker.viewmodel.ExpenseViewModel
import com.sree.smartexpensetracker.viewmodel.ExpenseViewModelFactory
import com.sree.smartexpensetracker.viewmodel.ExportViewModel
import com.sree.smartexpensetracker.viewmodel.ExportViewModelFactory
import com.sree.smartexpensetracker.viewmodel.InsightViewModel
import com.sree.smartexpensetracker.viewmodel.InsightViewModelFactory
import com.sree.smartexpensetracker.viewmodel.ReminderViewModel
import com.sree.smartexpensetracker.viewmodel.ReminderViewModelFactory

class MainActivity : ComponentActivity() {

    private val database: ExpenseDatabase by lazy {
        ExpenseDatabase.getDatabase(applicationContext)
    }

    private val transactionRepository: TransactionRepository by lazy {
        TransactionRepository(database.transactionDao())
    }

    private val budgetRepository: BudgetRepository by lazy {
        BudgetRepository(database.budgetDao())
    }

    private val reminderRepository: ReminderRepository by lazy {
        ReminderRepository(database.reminderDao())
    }

    private val analyticsRepository: AnalyticsRepository by lazy {
        AnalyticsRepository(database.transactionDao())
    }

    private val insightRepository: InsightRepository by lazy {
        InsightRepository(database.transactionDao())
    }

    private val exportRepository: ExportRepository by lazy {
        ExportRepository(applicationContext, transactionRepository)
    }

    private val expenseViewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory(transactionRepository)
    }

    private val budgetViewModel: BudgetViewModel by viewModels {
        BudgetViewModelFactory(budgetRepository)
    }

    private val analyticsViewModel: AnalyticsViewModel by viewModels {
        AnalyticsViewModelFactory(analyticsRepository)
    }

    private val insightViewModel: InsightViewModel by viewModels {
        InsightViewModelFactory(insightRepository)
    }

    private val reminderViewModel: ReminderViewModel by viewModels {
        ReminderViewModelFactory(reminderRepository, applicationContext)
    }

    private val exportViewModel: ExportViewModel by viewModels {
        ExportViewModelFactory(exportRepository)
    }

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        NotificationUtils.createNotificationChannel(this)
        requestNotificationPermissionIfNeeded()

        setContent {
            SmartExpenseTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navController = rememberNavController()

                    AppNavGraph(
                        navController = navController,
                        expenseViewModel = expenseViewModel,
                        budgetViewModel = budgetViewModel,
                        analyticsViewModel = analyticsViewModel,
                        insightViewModel = insightViewModel,
                        reminderViewModel = reminderViewModel,
                        exportViewModel = exportViewModel,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}