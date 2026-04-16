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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import com.sree.smartexpensetracker.ui.components.EmptyState
import com.sree.smartexpensetracker.ui.components.TopBarSection
import com.sree.smartexpensetracker.ui.components.UiUtils
import com.sree.smartexpensetracker.ui.theme.AccentCyan
import com.sree.smartexpensetracker.ui.theme.DarkCard
import com.sree.smartexpensetracker.ui.theme.GlassWhite
import com.sree.smartexpensetracker.ui.theme.PrimaryBlue
import com.sree.smartexpensetracker.ui.theme.TextPrimary
import com.sree.smartexpensetracker.ui.theme.TextSecondary
import com.sree.smartexpensetracker.viewmodel.ExportViewModel

@Composable
fun ExportScreen(
    exportViewModel: ExportViewModel,
    onMenuClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val exportResult by exportViewModel.exportResult.collectAsState()
    val isExporting by exportViewModel.isExporting.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(UiUtils.ScreenGradient),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            TopBarSection(
                title = "Export Report",
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
                )
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
                        .border(
                            width = 1.dp,
                            color = GlassWhite.copy(alpha = 0.10f),
                            shape = RoundedCornerShape(28.dp)
                        )
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Export Transactions",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary
                    )

                    Text(
                        text = "Generate premium CSV or PDF reports of your transactions.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )

                    Button(
                        onClick = { exportViewModel.exportCsv() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(18.dp),
                        enabled = !isExporting,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryBlue,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Export CSV")
                    }

                    Button(
                        onClick = { exportViewModel.exportPdf() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(18.dp),
                        enabled = !isExporting,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentCyan,
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Export PDF")
                    }
                }
            }
        }

        item {
            when {
                isExporting -> {
                    EmptyState(
                        title = "Export in progress",
                        description = "Preparing your report."
                    )
                }

                exportResult == null -> {
                    EmptyState(
                        title = "No export yet",
                        description = "Export your transactions as CSV or PDF."
                    )
                }

                else -> {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = DarkCard.copy(alpha = 0.95f)
                        )
                    ) {
                        androidx.compose.foundation.layout.Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = exportResult?.message.orEmpty(),
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary
                            )

                            exportResult?.exportType?.let {
                                Text(
                                    text = "Type: $it",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondary
                                )
                            }

                            exportResult?.filePath?.let {
                                Text(
                                    text = "Saved at:\n$it",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = AccentCyan
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}