package com.sree.smartexpensetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sree.smartexpensetracker.ui.theme.AccentCyan
import com.sree.smartexpensetracker.ui.theme.TextPrimary

@Composable
fun CategoryChip(
    label: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = label,
        modifier = modifier
            .background(
                color = AccentCyan.copy(alpha = 0.15f),
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 14.dp, vertical = 8.dp),
        color = TextPrimary,
        style = MaterialTheme.typography.bodyMedium
    )
}