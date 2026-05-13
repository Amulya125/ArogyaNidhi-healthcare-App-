package com.example.arogyanidhi.ui.schemes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchemeListScreen(viewModel: SchemeViewModel, onNavigateBack: () -> Unit, onSchemeClick: (String) -> Unit) {
    val schemes by viewModel.schemes.collectAsState()
    val categories = listOf("All") + schemes.map { it.category }.distinct()
    var selected by remember { mutableStateOf("All") }
    val filtered = if (selected == "All") schemes else schemes.filter { it.category == selected }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Government Schemes") },
            navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }
        )
    }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    FilterChip(selected = selected == cat, onClick = { selected = cat }, label = { Text(cat) })
                }
            }
            LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(filtered) { scheme ->
                    Card(onClick = { onSchemeClick(scheme.id) }, modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp)) {
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(scheme.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                Spacer(Modifier.height(4.dp))
                                Text(scheme.description, style = MaterialTheme.typography.bodySmall, maxLines = 2, color = MaterialTheme.colorScheme.secondary)
                                Spacer(Modifier.height(8.dp))
                                AssistChip(onClick = {}, label = { Text(scheme.category, style = MaterialTheme.typography.labelSmall) })
                            }
                            Icon(Icons.Default.ChevronRight, null, tint = MaterialTheme.colorScheme.secondary)
                        }
                    }
                }
            }
        }
    }
}
