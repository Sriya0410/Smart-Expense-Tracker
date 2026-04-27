package com.sree.smartexpensetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sree.smartexpensetracker.navigation.Screen
import com.sree.smartexpensetracker.ui.theme.AccentCyan
import com.sree.smartexpensetracker.ui.theme.DarkBackground
import com.sree.smartexpensetracker.ui.theme.DarkSurface
import com.sree.smartexpensetracker.ui.theme.GlassBorder
import com.sree.smartexpensetracker.ui.theme.PrimaryBlue
import com.sree.smartexpensetracker.ui.theme.TextPrimary
import com.sree.smartexpensetracker.ui.theme.TextSecondary

private data class DrawerDestination(
    val route: String,
    val label: String,
    val icon: ImageVector
)

@Composable
fun AppDrawerContent(
    currentRoute: String?,
    onItemClick: (String) -> Unit
) {
    val items = listOf(
        DrawerDestination(Screen.Dashboard.route, "Dashboard", Icons.Default.Dashboard),
        DrawerDestination(Screen.AddTransaction.route, "Add Transaction", Icons.Default.AddCard),
        DrawerDestination(Screen.TransactionHistory.route, "Transaction History", Icons.Default.History),
        DrawerDestination(Screen.Budget.route, "Budget Management", Icons.Default.Wallet),
        DrawerDestination(Screen.Analytics.route, "Analytics", Icons.Default.BarChart),
        DrawerDestination(Screen.Insights.route, "Smart Insights", Icons.Default.Lightbulb),
        DrawerDestination(Screen.Export.route, "Export Report", Icons.Default.FileDownload)
    )

    ModalDrawerSheet(
        modifier = Modifier.fillMaxHeight(),
        drawerContainerColor = DarkSurface,
        drawerContentColor = TextPrimary
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(DarkSurface, DarkBackground)
                    )
                )
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(18.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = PrimaryBlue.copy(alpha = 0.10f),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(18.dp)
            ) {
                Text(
                    text = "Smart Expense Tracker",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Premium Finance Dashboard",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                HorizontalDivider(
                    thickness = 1.dp,
                    color = GlassBorder
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Track spending, budgets, insights, and exports in one place.",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            items.forEach { item ->
                val selected = currentRoute == item.route

                NavigationDrawerItem(
                    label = {
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextPrimary
                        )
                    },
                    selected = selected,
                    onClick = { onItemClick(item.route) },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = if (selected) AccentCyan else TextSecondary
                        )
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = PrimaryBlue.copy(alpha = 0.18f),
                        unselectedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                        selectedTextColor = TextPrimary,
                        unselectedTextColor = TextSecondary,
                        selectedIconColor = AccentCyan,
                        unselectedIconColor = TextSecondary
                    ),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}