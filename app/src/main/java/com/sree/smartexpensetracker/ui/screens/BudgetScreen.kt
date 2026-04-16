package com.sree.smartexpensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.sree.smartexpensetracker.ui.components.BudgetCard
import com.sree.smartexpensetracker.ui.components.EmptyState
import com.sree.smartexpensetracker.ui.components.TopBarSection
import com.sree.smartexpensetracker.ui.components.UiUtils
import com.sree.smartexpensetracker.ui.theme.AccentCyan
import com.sree.smartexpensetracker.ui.theme.DarkCard
import com.sree.smartexpensetracker.ui.theme.GlassBorder
import com.sree.smartexpensetracker.ui.theme.GlassWhite
import com.sree.smartexpensetracker.ui.theme.PrimaryBlue
import com.sree.smartexpensetracker.ui.theme.TextPrimary
import com.sree.smartexpensetracker.ui.theme.TextSecondary
import com.sree.smartexpensetracker.viewmodel.BudgetViewModel

@Composable
fun BudgetScreen(
    budgetViewModel: BudgetViewModel,
    onMenuClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val budgets by budgetViewModel.budgets.collectAsState()
    val message by budgetViewModel.message.collectAsState()

    val category = remember { mutableStateOf("") }
    val amount = remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(UiUtils.ScreenGradient),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            TopBarSection(
                title = "Budget Management",
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
                androidx.compose.foundation.layout.Column(
                    modifier = Modifier
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
                        .border(
                            width = 1.dp,
                            color = GlassWhite.copy(alpha = 0.10f),
                            shape = RoundedCornerShape(28.dp)
                        )
                        .padding(22.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Set Monthly Budgets",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary
                    )

                    Text(
                        text = "Create category-wise budgets and monitor your spending progress with a premium visual overview.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )

                    if (!message.isNullOrBlank()) {
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = AccentCyan.copy(alpha = 0.12f)
                            )
                        ) {
                            Text(
                                text = message ?: "",
                                modifier = Modifier.padding(12.dp),
                                color = TextPrimary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    OutlinedTextField(
                        value = category.value,
                        onValueChange = { category.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Category") },
                        placeholder = { Text("Ex: Food, Travel, Shopping") },
                        singleLine = true,
                        shape = RoundedCornerShape(18.dp),
                        colors = budgetFieldColors()
                    )

                    OutlinedTextField(
                        value = amount.value,
                        onValueChange = { amount.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Budget Amount") },
                        placeholder = { Text("Enter monthly budget") },
                        singleLine = true,
                        shape = RoundedCornerShape(18.dp),
                        colors = budgetFieldColors()
                    )

                    Button(
                        onClick = {
                            val parsed = amount.value.toDoubleOrNull() ?: 0.0
                            if (category.value.isNotBlank() && parsed > 0.0) {
                                budgetViewModel.addBudget(category.value.trim(), parsed)
                                category.value = ""
                                amount.value = ""
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryBlue,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Save Budget",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "Your Budget Overview",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
        }

        if (budgets.isEmpty()) {
            item {
                EmptyState(
                    title = "No budgets added",
                    description = "Create your first category budget to start tracking monthly spending."
                )
            }
        } else {
            items(budgets) { budget ->
                BudgetCard(
                    budget = budget,
                    spentAmount = 0.0
                )
            }
        }
    }
}

@Composable
private fun budgetFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    focusedBorderColor = AccentCyan,
    unfocusedBorderColor = GlassBorder,
    cursorColor = AccentCyan,
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    focusedLabelColor = AccentCyan,
    unfocusedLabelColor = TextSecondary,
    focusedPlaceholderColor = TextSecondary,
    unfocusedPlaceholderColor = TextSecondary
)