package com.sree.smartexpensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sree.smartexpensetracker.data.local.TransactionType
import com.sree.smartexpensetracker.ui.components.EmptyState
import com.sree.smartexpensetracker.ui.components.FilterBottomSheet
import com.sree.smartexpensetracker.ui.components.SearchBar
import com.sree.smartexpensetracker.ui.components.TopBarSection
import com.sree.smartexpensetracker.ui.components.TransactionItem
import com.sree.smartexpensetracker.ui.components.UiUtils
import com.sree.smartexpensetracker.ui.theme.AccentCyan
import com.sree.smartexpensetracker.ui.theme.DarkCard
import com.sree.smartexpensetracker.ui.theme.GlassWhite
import com.sree.smartexpensetracker.ui.theme.PrimaryBlue
import com.sree.smartexpensetracker.ui.theme.TextPrimary
import com.sree.smartexpensetracker.ui.theme.TextSecondary
import com.sree.smartexpensetracker.viewmodel.ExpenseViewModel

@Composable
fun TransactionHistoryScreen(
    expenseViewModel: ExpenseViewModel,
    onMenuClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val transactions by expenseViewModel.transactions.collectAsState()
    val filterState by expenseViewModel.filterState.collectAsState()

    val showFilters = remember { mutableStateOf(false) }
    val categoryInput = remember { mutableStateOf(filterState.category ?: "") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(UiUtils.ScreenGradient),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            TopBarSection(
                title = "Transaction History",
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
                    containerColor = DarkCard.copy(alpha = 0.95f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    PrimaryBlue.copy(alpha = 0.16f),
                                    GlassWhite.copy(alpha = 0.05f),
                                    Color.Transparent
                                )
                            ),
                            shape = RoundedCornerShape(28.dp)
                        )
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Search & Explore",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary
                    )

                    Text(
                        text = "Review every income and expense entry with quick search and smart filters.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )

                    SearchBar(
                        query = filterState.searchQuery,
                        onQueryChange = { expenseViewModel.updateSearchQuery(it) }
                    )

                    IconButton(onClick = { showFilters.value = !showFilters.value }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Toggle Filters",
                            tint = AccentCyan
                        )
                    }
                }
            }
        }

        if (showFilters.value) {
            item {
                FilterBottomSheet(
                    category = categoryInput.value,
                    onCategoryChange = {
                        categoryInput.value = it
                        expenseViewModel.updateCategory(it.ifBlank { null })
                    },
                    onIncomeClick = { expenseViewModel.updateType(TransactionType.INCOME) },
                    onExpenseClick = { expenseViewModel.updateType(TransactionType.EXPENSE) },
                    onClearClick = {
                        categoryInput.value = ""
                        expenseViewModel.clearFilters()
                    }
                )
            }
        }

        item {
            Text(
                text = "All Transactions",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
        }

        if (transactions.isEmpty()) {
            item {
                EmptyState(
                    title = "No transactions found",
                    description = "Add transactions or adjust your filters to see results."
                )
            }
        } else {
            items(transactions) { transaction ->
                TransactionItem(transaction = transaction)
            }
        }
    }
}