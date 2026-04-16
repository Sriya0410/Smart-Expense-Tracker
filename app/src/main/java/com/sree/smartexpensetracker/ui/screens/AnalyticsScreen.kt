package com.sree.smartexpensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.sree.smartexpensetracker.ui.components.DonutChart
import com.sree.smartexpensetracker.ui.components.EmptyState
import com.sree.smartexpensetracker.ui.components.SimpleBarChart
import com.sree.smartexpensetracker.ui.components.SummaryCard
import com.sree.smartexpensetracker.ui.components.TopBarSection
import com.sree.smartexpensetracker.ui.components.UiUtils
import com.sree.smartexpensetracker.ui.theme.DarkCard
import com.sree.smartexpensetracker.ui.theme.ExpenseRed
import com.sree.smartexpensetracker.ui.theme.GlassWhite
import com.sree.smartexpensetracker.ui.theme.IncomeGreen
import com.sree.smartexpensetracker.ui.theme.PrimaryBlue
import com.sree.smartexpensetracker.ui.theme.TextPrimary
import com.sree.smartexpensetracker.ui.theme.TextSecondary
import com.sree.smartexpensetracker.viewmodel.AnalyticsViewModel

@Composable
fun AnalyticsScreen(
    analyticsViewModel: AnalyticsViewModel,
    onMenuClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val summary by analyticsViewModel.summary.collectAsState()
    val categoryExpenses by analyticsViewModel.categoryExpenses.collectAsState()
    val monthlyExpenseTotal by analyticsViewModel.monthlyExpenseTotal.collectAsState()
    val weeklyExpenseTotal by analyticsViewModel.weeklyExpenseTotal.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(UiUtils.ScreenGradient),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            TopBarSection(
                title = "Analytics",
                showMenu = true,
                showBack = false,
                onMenuClick = onMenuClick
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = DarkCard.copy(alpha = 0.95f)
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
                        text = "Spending Analytics",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Track your financial performance with category breakdowns, expense summaries, and monthly spending insights.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
        }

        item {
            SummaryCard(
                title = "Total Balance",
                amount = summary.balance,
                subtitle = "Current balance snapshot",
                amountColor = if (summary.balance >= 0) TextPrimary else ExpenseRed
            )
        }

        item {
            SummaryCard(
                title = "This Month Expense",
                amount = monthlyExpenseTotal,
                subtitle = "Current month total spending",
                amountColor = ExpenseRed
            )
        }

        item {
            SummaryCard(
                title = "This Week Expense",
                amount = weeklyExpenseTotal,
                subtitle = "Current week spending",
                amountColor = IncomeGreen
            )
        }

        item {
            Text(
                text = "Visual Breakdown",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
        }

        if (categoryExpenses.isEmpty()) {
            item {
                EmptyState(
                    title = "No analytics data",
                    description = "Add expense transactions to unlock charts and trend analysis."
                )
            }
        } else {
            item {
                DonutChart(
                    items = categoryExpenses,
                    title = "Category-wise Expense Distribution"
                )
            }

            item {
                SimpleBarChart(
                    items = categoryExpenses,
                    title = "Expense by Category"
                )
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = DarkCard.copy(alpha = 0.94f)
                )
            ) {
                androidx.compose.foundation.layout.Column(
                    modifier = Modifier
                        .background(GlassWhite.copy(alpha = 0.04f))
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Summary Insight",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )

                    Text(
                        text = if (summary.balance >= 0) {
                            "Your financial balance is currently healthy. Keep monitoring your major expense categories to maintain steady growth."
                        } else {
                            "Your expenses are exceeding your balance. Review your top categories and set stricter budgets to regain control."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}