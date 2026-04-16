package com.sree.smartexpensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sree.smartexpensetracker.ui.components.EmptyState
import com.sree.smartexpensetracker.ui.components.InsightCard
import com.sree.smartexpensetracker.ui.components.SummaryCard
import com.sree.smartexpensetracker.ui.components.TopBarSection
import com.sree.smartexpensetracker.ui.components.TransactionItem
import com.sree.smartexpensetracker.ui.components.UiUtils
import com.sree.smartexpensetracker.ui.theme.AccentCyan
import com.sree.smartexpensetracker.ui.theme.DarkCard
import com.sree.smartexpensetracker.ui.theme.ExpenseRed
import com.sree.smartexpensetracker.ui.theme.GlassWhite
import com.sree.smartexpensetracker.ui.theme.IncomeGreen
import com.sree.smartexpensetracker.ui.theme.PrimaryBlue
import com.sree.smartexpensetracker.ui.theme.TextPrimary
import com.sree.smartexpensetracker.ui.theme.TextSecondary
import com.sree.smartexpensetracker.viewmodel.AnalyticsViewModel
import com.sree.smartexpensetracker.viewmodel.ExpenseViewModel
import com.sree.smartexpensetracker.viewmodel.InsightViewModel

@Composable
fun DashboardScreen(
    expenseViewModel: ExpenseViewModel,
    analyticsViewModel: AnalyticsViewModel,
    insightViewModel: InsightViewModel,
    onMenuClick: () -> Unit,
    onAddTransactionClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onBudgetClick: () -> Unit,
    onAnalyticsClick: () -> Unit
) {
    val transactions by expenseViewModel.transactions.collectAsState()
    val totalIncome by expenseViewModel.totalIncome.collectAsState()
    val totalExpense by expenseViewModel.totalExpense.collectAsState()
    val insights by insightViewModel.insights.collectAsState()
    val summary by analyticsViewModel.summary.collectAsState()

    val balance = totalIncome - totalExpense

    LazyColumn(
        modifier = Modifier.background(UiUtils.ScreenGradient),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            TopBarSection(
                title = "Dashboard",
                showMenu = true,
                showBack = false,
                onMenuClick = onMenuClick
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = DarkCard.copy(alpha = 0.94f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                androidx.compose.foundation.layout.Column(
                    modifier = Modifier
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    PrimaryBlue.copy(alpha = 0.16f),
                                    GlassWhite.copy(alpha = 0.05f),
                                    Color.Transparent
                                )
                            )
                        )
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Financial Overview",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Manage your money with smart analytics, reminders, budgeting tools, and a premium expense experience.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )

                    Text(
                        text = if (summary.isPositiveBalance) {
                            "You are maintaining a positive balance this cycle."
                        } else {
                            "Your spending is currently higher than your available balance."
                        },
                        style = MaterialTheme.typography.labelLarge,
                        color = if (summary.isPositiveBalance) IncomeGreen else ExpenseRed
                    )
                }
            }
        }

        item {
            SummaryCard(
                title = "Total Balance",
                amount = balance,
                subtitle = if (balance >= 0) "Current available balance" else "Negative balance alert",
                amountColor = if (balance >= 0) TextPrimary else ExpenseRed
            )
        }

        item {
            SummaryCard(
                title = "Total Income",
                amount = totalIncome,
                subtitle = "All recorded income",
                amountColor = IncomeGreen
            )
        }

        item {
            SummaryCard(
                title = "Total Expense",
                amount = totalExpense,
                subtitle = "All recorded expenses",
                amountColor = ExpenseRed
            )
        }

        item {
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
        }

        item {
            androidx.compose.foundation.layout.Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onAddTransactionClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryBlue,
                        contentColor = Color.White
                    )
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Text("  Add Transaction", fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = onHistoryClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentCyan,
                        contentColor = Color.Black
                    )
                ) {
                    Icon(Icons.Default.History, contentDescription = null)
                    Text("  Transaction History", fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = onBudgetClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GlassWhite.copy(alpha = 0.14f),
                        contentColor = TextPrimary
                    )
                ) {
                    Icon(Icons.Default.Wallet, contentDescription = null)
                    Text("  Budget Management", fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = onAnalyticsClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GlassWhite.copy(alpha = 0.14f),
                        contentColor = TextPrimary
                    )
                ) {
                    Icon(Icons.Default.BarChart, contentDescription = null)
                    Text("  Analytics", fontWeight = FontWeight.SemiBold)
                }
            }
        }

        if (insights.isNotEmpty()) {
            item {
                Text(
                    text = "Smart Insights",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            items(insights.take(2)) { insight ->
                InsightCard(insight = insight)
            }
        }

        item {
            Text(
                text = "Recent Transactions",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (transactions.isEmpty()) {
            item {
                EmptyState(
                    title = "No transactions yet",
                    description = "Start by adding your first income or expense to unlock analytics, insights, and budgets."
                )
            }
        } else {
            items(transactions.take(5)) { transaction ->
                TransactionItem(transaction = transaction)
            }
        }


    }
}