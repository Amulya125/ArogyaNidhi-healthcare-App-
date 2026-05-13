package com.example.arogyanidhi.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigateToEligibility: () -> Unit,
    onNavigateToSchemes: () -> Unit,
    onNavigateToHospitals: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToChatbot: () -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Arogya Nidhi", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, "Settings", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToChatbot,
                icon = { Icon(Icons.Default.SmartToy, "AI Assistant") },
                text = { Text("ArogyaBot") },
                containerColor = MaterialTheme.colorScheme.primary
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp)) {
                Text("Hello,", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.secondary)
                Text(
                    userProfile?.name?.ifBlank { "User" } ?: "User",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(Modifier.height(4.dp))
                Text("How can we help you today?", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
            }

            val menuItems = listOf(
                DashboardMenuItem("Find Hospitals", Icons.Default.LocalHospital, Color(0xFFE57373), onNavigateToHospitals),
                DashboardMenuItem("Health Schemes", Icons.Default.Assignment, Color(0xFF64B5F6), onNavigateToSchemes),
                DashboardMenuItem("Eligibility Check", Icons.Default.CheckCircle, Color(0xFF81C784), onNavigateToEligibility),
                DashboardMenuItem("My Profile", Icons.Default.Person, Color(0xFFFFB74D), onNavigateToProfile)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(menuItems) { item ->
                    Surface(
                        onClick = item.onClick,
                        modifier = Modifier.height(150.dp).fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 4.dp,
                        shadowElevation = 6.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier.size(52.dp).clip(CircleShape).background(item.color.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(item.icon, null, tint = item.color, modifier = Modifier.size(26.dp))
                            }
                            Spacer(Modifier.height(12.dp))
                            Text(item.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                        }
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(Modifier.padding(20.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.HealthAndSafety, null, tint = Color.White, modifier = Modifier.size(40.dp))
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text("Stay Safe & Healthy", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Text("Tap ArogyaBot below to ask about schemes.", color = Color.White.copy(alpha = 0.85f), style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            Spacer(Modifier.height(80.dp))
        }
    }
}

data class DashboardMenuItem(val title: String, val icon: ImageVector, val color: Color, val onClick: () -> Unit)
