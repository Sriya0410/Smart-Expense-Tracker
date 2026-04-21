package com.sree.smartexpensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
import com.sree.smartexpensetracker.viewmodel.ReminderViewModel

@Composable
fun ReminderScreen(
    reminderViewModel: ReminderViewModel,
    onMenuClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val reminders by reminderViewModel.reminders.collectAsState()
    val messageState by reminderViewModel.message.collectAsState()

    val title = remember { mutableStateOf("") }
    val message = remember { mutableStateOf("") }
    val hour = remember { mutableStateOf("") }
    val minute = remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(UiUtils.ScreenGradient),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            TopBarSection(
                title = "Reminders",
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
                )
            ) {
                Column(
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
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Daily Reminder Setup",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary
                    )

                    Text(
                        text = "Set premium reminders to track expenses consistently and never miss an update.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )

                    if (!messageState.isNullOrBlank()) {
                        Text(
                            text = messageState.orEmpty(),
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    OutlinedTextField(
                        value = title.value,
                        onValueChange = { title.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Reminder Title") },
                        singleLine = true,
                        shape = RoundedCornerShape(18.dp),
                        colors = reminderFieldColors()
                    )

                    OutlinedTextField(
                        value = message.value,
                        onValueChange = { message.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Reminder Message") },
                        shape = RoundedCornerShape(18.dp),
                        colors = reminderFieldColors()
                    )

                    OutlinedTextField(
                        value = hour.value,
                        onValueChange = { hour.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Hour (0-23)") },
                        singleLine = true,
                        shape = RoundedCornerShape(18.dp),
                        colors = reminderFieldColors()
                    )

                    OutlinedTextField(
                        value = minute.value,
                        onValueChange = { minute.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Minute (0-59)") },
                        singleLine = true,
                        shape = RoundedCornerShape(18.dp),
                        colors = reminderFieldColors()
                    )

                    Button(
                        onClick = {
                            val parsedHour = hour.value.toIntOrNull()
                            val parsedMinute = minute.value.toIntOrNull()

                            if (
                                title.value.isNotBlank() &&
                                message.value.isNotBlank() &&
                                parsedHour != null &&
                                parsedMinute != null
                            ) {
                                reminderViewModel.addReminder(
                                    title = title.value.trim(),
                                    message = message.value.trim(),
                                    hour = parsedHour,
                                    minute = parsedMinute
                                )
                                title.value = ""
                                message.value = ""
                                hour.value = ""
                                minute.value = ""
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
                            text = "Save Reminder",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }

        if (reminders.isEmpty()) {
            item {
                EmptyState(
                    title = "No reminders added",
                    description = "Set daily reminders to track expenses."
                )
            }
        } else {
            items(reminders) { reminder ->
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = DarkCard.copy(alpha = 0.95f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(0.8f),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = reminder.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = reminder.message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                            Text(
                                text = String.format("%02d:%02d", reminder.hour, reminder.minute),
                                color = AccentCyan
                            )
                        }

                        Switch(
                            checked = reminder.isEnabled,
                            onCheckedChange = {
                                reminderViewModel.toggleReminder(reminder, it)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun reminderFieldColors() = OutlinedTextFieldDefaults.colors(
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