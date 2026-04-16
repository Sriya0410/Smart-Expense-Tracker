package com.sree.smartexpensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sree.smartexpensetracker.data.local.TransactionType
import com.sree.smartexpensetracker.ui.components.TopBarSection
import com.sree.smartexpensetracker.ui.components.UiUtils
import com.sree.smartexpensetracker.ui.theme.AccentCyan
import com.sree.smartexpensetracker.ui.theme.DarkCard
import com.sree.smartexpensetracker.ui.theme.ExpenseRed
import com.sree.smartexpensetracker.ui.theme.GlassBorder
import com.sree.smartexpensetracker.ui.theme.GlassWhite
import com.sree.smartexpensetracker.ui.theme.IncomeGreen
import com.sree.smartexpensetracker.ui.theme.PrimaryBlue
import com.sree.smartexpensetracker.ui.theme.TextPrimary
import com.sree.smartexpensetracker.ui.theme.TextSecondary
import com.sree.smartexpensetracker.viewmodel.ExpenseViewModel

@Composable
fun AddTransactionScreen(
    expenseViewModel: ExpenseViewModel,
    onMenuClick: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(TransactionType.EXPENSE) }

    val message by expenseViewModel.message.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiUtils.ScreenGradient)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TopBarSection(
            title = "Add Transaction",
            showMenu = true,
            showBack = false,
            onMenuClick = onMenuClick
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
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
                    .border(
                        width = 1.dp,
                        color = GlassWhite.copy(alpha = 0.10f),
                        shape = RoundedCornerShape(28.dp)
                    )
                    .padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Text(
                    text = "Create a premium entry",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Add income or expenses with category and notes to keep your financial dashboard smart and organized.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                if (!message.isNullOrBlank()) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
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
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Title") },
                    placeholder = { Text("Ex: Grocery shopping") },
                    singleLine = true,
                    shape = RoundedCornerShape(18.dp),
                    colors = textFieldColors()
                )

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Amount") },
                    placeholder = { Text("Ex: 2500") },
                    singleLine = true,
                    shape = RoundedCornerShape(18.dp),
                    colors = textFieldColors()
                )

                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Category") },
                    placeholder = { Text("Ex: Food, Travel, Salary") },
                    singleLine = true,
                    shape = RoundedCornerShape(18.dp),
                    colors = textFieldColors()
                )

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    label = { Text("Note") },
                    placeholder = { Text("Optional note") },
                    shape = RoundedCornerShape(18.dp),
                    colors = textFieldColors()
                )

                Text(
                    text = "Transaction Type",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedType == TransactionType.EXPENSE) {
                                ExpenseRed.copy(alpha = 0.18f)
                            } else {
                                GlassWhite.copy(alpha = 0.06f)
                            }
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            width = 1.dp,
                            color = if (selectedType == TransactionType.EXPENSE) {
                                ExpenseRed.copy(alpha = 0.35f)
                            } else {
                                GlassWhite.copy(alpha = 0.08f)
                            }
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            RadioButton(
                                selected = selectedType == TransactionType.EXPENSE,
                                onClick = { selectedType = TransactionType.EXPENSE }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Expense",
                                color = TextPrimary,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(top = 12.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedType == TransactionType.INCOME) {
                                IncomeGreen.copy(alpha = 0.18f)
                            } else {
                                GlassWhite.copy(alpha = 0.06f)
                            }
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            width = 1.dp,
                            color = if (selectedType == TransactionType.INCOME) {
                                IncomeGreen.copy(alpha = 0.35f)
                            } else {
                                GlassWhite.copy(alpha = 0.08f)
                            }
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            RadioButton(
                                selected = selectedType == TransactionType.INCOME,
                                onClick = { selectedType = TransactionType.INCOME }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Income",
                                color = TextPrimary,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(top = 12.dp)
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        val parsedAmount = amount.toDoubleOrNull() ?: 0.0

                        if (title.isNotBlank() && category.isNotBlank() && parsedAmount > 0.0) {
                            expenseViewModel.addTransaction(
                                title = title.trim(),
                                amount = parsedAmount,
                                category = category.trim(),
                                note = note.trim(),
                                type = selectedType
                            )

                            title = ""
                            amount = ""
                            category = ""
                            note = ""
                            selectedType = TransactionType.EXPENSE
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryBlue,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Text(
                        text = "Save Transaction",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun textFieldColors() = OutlinedTextFieldDefaults.colors(
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