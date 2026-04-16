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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sree.smartexpensetracker.data.model.InsightItem
import com.sree.smartexpensetracker.data.model.InsightSeverity
import com.sree.smartexpensetracker.ui.theme.AccentCyan
import com.sree.smartexpensetracker.ui.theme.DarkCard
import com.sree.smartexpensetracker.ui.theme.ExpenseRed
import com.sree.smartexpensetracker.ui.theme.GlassWhite
import com.sree.smartexpensetracker.ui.theme.IncomeGreen
import com.sree.smartexpensetracker.ui.theme.TextPrimary
import com.sree.smartexpensetracker.ui.theme.TextSecondary

@Composable
fun InsightCard(
    insight: InsightItem,
    modifier: Modifier = Modifier
) {
    val accentColor = when (insight.severity) {
        InsightSeverity.INFO -> AccentCyan
        InsightSeverity.WARNING -> ExpenseRed
        InsightSeverity.POSITIVE -> IncomeGreen
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
                    color = GlassWhite.copy(alpha = 0.04f),
                    shape = RoundedCornerShape(22.dp)
                )
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = insight.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                if (insight.highlightValue.isNotBlank()) {
                    Text(
                        text = insight.highlightValue,
                        style = MaterialTheme.typography.labelLarge,
                        color = accentColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Text(
                text = insight.description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            if (insight.recommendation.isNotBlank()) {
                Text(
                    text = insight.recommendation,
                    style = MaterialTheme.typography.labelLarge,
                    color = accentColor,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}