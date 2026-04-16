package com.sree.smartexpensetracker.ui.components

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.sree.smartexpensetracker.ui.theme.AccentCyan
import com.sree.smartexpensetracker.ui.theme.DarkBackground
import com.sree.smartexpensetracker.ui.theme.DarkCard
import com.sree.smartexpensetracker.ui.theme.ExpenseRed
import com.sree.smartexpensetracker.ui.theme.GlassWhite
import com.sree.smartexpensetracker.ui.theme.GradientEnd
import com.sree.smartexpensetracker.ui.theme.GradientStart
import com.sree.smartexpensetracker.ui.theme.IncomeGreen
import com.sree.smartexpensetracker.ui.theme.PrimaryBlue

object UiUtils {

    val ScreenGradient = Brush.verticalGradient(
        colors = listOf(
            DarkBackground,
            GradientStart,
            GradientEnd
        )
    )

    val CardGradient = Brush.linearGradient(
        colors = listOf(
            PrimaryBlue.copy(alpha = 0.18f),
            GlassWhite.copy(alpha = 0.08f)
        )
    )

    val IncomeColor = IncomeGreen
    val ExpenseColor = ExpenseRed
    val AccentColor = AccentCyan
    val WarningColor = Color(0xFFF59E0B)
    val CardBackground = DarkCard
}