package com.sree.smartexpensetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sree.smartexpensetracker.data.local.BudgetEntity
import com.sree.smartexpensetracker.ui.theme.DarkCard
import com.sree.smartexpensetracker.ui.theme.ExpenseRed
import com.sree.smartexpensetracker.ui.theme.GlassWhite
import com.sree.smartexpensetracker.ui.theme.IncomeGreen
import com.sree.smartexpensetracker.ui.theme.PrimaryBlue
import com.sree.smartexpensetracker.ui.theme.TextPrimary
import com.sree.smartexpensetracker.ui.theme.TextSecondary
import com.sree.smartexpensetracker.utils.CurrencyUtils
import kotlin.math.abs

@Composable
fun BudgetCard(
    budget: BudgetEntity,
    spentAmount: Double = 0.0,
    modifier: Modifier = Modifier
) {
    val rawProgress = if (budget.limitAmount > 0) {
        (spentAmount / budget.limitAmount).toFloat()
    } else {
        0f
    }

    val progress = rawProgress.coerceIn(0f, 1f)
    val remaining = budget.limitAmount - spentAmount
    val isOverBudget = spentAmount > budget.limitAmount

    val progressColor = when {
        rawProgress < 0.6f -> IncomeGreen
        rawProgress < 1f -> PrimaryBlue
        else -> ExpenseRed
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkCard.copy(alpha = 0.96f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            GlassWhite.copy(alpha = 0.06f),
                            DarkCard.copy(alpha = 0.98f)
                        )
                    )
                )
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = budget.category,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Budget Limit",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary
                    )
                    Text(
                        text = CurrencyUtils.format(budget.limitAmount),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Spent",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary
                    )
                    Text(
                        text = CurrencyUtils.format(spentAmount),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = if (isOverBudget) {
                        "Budget exceeded by ${CurrencyUtils.format(abs(remaining))}"
                    } else {
                        "Remaining ${CurrencyUtils.format(remaining)}"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isOverBudget) ExpenseRed else IncomeGreen,
                    fontWeight = FontWeight.Medium
                )

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth(),
                    color = progressColor,
                    trackColor = GlassWhite.copy(alpha = 0.12f)
                )

                Text(
                    text = "${"%.0f".format(rawProgress * 100)}% used",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isOverBudget) ExpenseRed else TextSecondary
                )
            }
        }
    }
}