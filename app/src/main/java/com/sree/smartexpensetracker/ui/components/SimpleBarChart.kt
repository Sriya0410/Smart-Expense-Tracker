package com.sree.smartexpensetracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.dp
import com.sree.smartexpensetracker.data.model.CategoryExpense
import com.sree.smartexpensetracker.ui.theme.AccentCyan
import com.sree.smartexpensetracker.ui.theme.DarkCard
import com.sree.smartexpensetracker.ui.theme.GlassWhite
import com.sree.smartexpensetracker.ui.theme.TextPrimary
import com.sree.smartexpensetracker.ui.theme.TextSecondary
import com.sree.smartexpensetracker.utils.CurrencyUtils

@Composable
fun SimpleBarChart(
    items: List<CategoryExpense>,
    modifier: Modifier = Modifier,
    title: String = "Category Expenses"
) {
    val maxAmount = items.maxOfOrNull { it.amount } ?: 1.0

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
                    color = GlassWhite.copy(alpha = 0.04f),
                    shape = RoundedCornerShape(22.dp)
                )
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )

            if (items.isEmpty()) {
                Text(
                    text = "No chart data available yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            } else {
                items.forEach { item ->
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item.category,
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextPrimary
                            )
                            Text(
                                text = CurrencyUtils.format(item.amount),
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }

                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(18.dp)
                        ) {
                            drawRoundRect(
                                color = GlassWhite.copy(alpha = 0.10f),
                                cornerRadius = CornerRadius(14f, 14f),
                                style = Fill
                            )

                            drawRoundRect(
                                color = AccentCyan,
                                size = size.copy(
                                    width = ((item.amount / maxAmount).toFloat() * size.width)
                                        .coerceAtLeast(0f)
                                ),
                                cornerRadius = CornerRadius(14f, 14f),
                                style = Fill
                            )
                        }
                    }
                }
            }
        }
    }
}