package com.sree.smartexpensetracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.sree.smartexpensetracker.data.model.CategoryExpense
import com.sree.smartexpensetracker.ui.theme.AccentCyan
import com.sree.smartexpensetracker.ui.theme.DarkCard
import com.sree.smartexpensetracker.ui.theme.ExpenseRed
import com.sree.smartexpensetracker.ui.theme.GlassWhite
import com.sree.smartexpensetracker.ui.theme.IncomeGreen
import com.sree.smartexpensetracker.ui.theme.PrimaryBlue
import com.sree.smartexpensetracker.ui.theme.TextPrimary
import com.sree.smartexpensetracker.ui.theme.TextSecondary
import com.sree.smartexpensetracker.utils.CurrencyUtils

@Composable
fun DonutChart(
    items: List<CategoryExpense>,
    modifier: Modifier = Modifier,
    title: String = "Expense Breakdown"
) {
    val total = items.sumOf { it.amount }
    val chartColors = listOf(
        PrimaryBlue,
        AccentCyan,
        IncomeGreen,
        ExpenseRed,
        Color(0xFFA78BFA),
        Color(0xFFF59E0B),
        Color(0xFF14B8A6)
    )

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

            if (items.isEmpty() || total <= 0.0) {
                Text(
                    text = "No expense data available yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(
                        modifier = Modifier.size(220.dp)
                    ) {
                        val strokeWidth = 42f
                        val diameter = size.minDimension
                        val arcSize = Size(diameter, diameter)

                        var startAngle = -90f

                        items.forEachIndexed { index, item ->
                            val sweepAngle = ((item.amount / total) * 360f).toFloat()

                            drawArc(
                                color = chartColors[index % chartColors.size],
                                startAngle = startAngle,
                                sweepAngle = sweepAngle,
                                useCenter = false,
                                topLeft = Offset.Zero,
                                size = arcSize,
                                style = Stroke(
                                    width = strokeWidth,
                                    cap = StrokeCap.Round
                                )
                            )
                            startAngle += sweepAngle
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Total",
                            style = MaterialTheme.typography.labelLarge,
                            color = TextSecondary
                        )
                        Text(
                            text = CurrencyUtils.format(total),
                            style = MaterialTheme.typography.titleLarge,
                            color = TextPrimary
                        )
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items.forEachIndexed { index, item ->
                        val color = chartColors[index % chartColors.size]
                        val percentage = if (total > 0) (item.amount / total) * 100 else 0.0

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(color = color, shape = CircleShape)
                                )

                                Spacer(modifier = Modifier.size(10.dp))

                                Text(
                                    text = item.category,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = TextPrimary
                                )
                            }

                            Text(
                                text = "${percentage.toInt()}% • ${CurrencyUtils.format(item.amount)}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}