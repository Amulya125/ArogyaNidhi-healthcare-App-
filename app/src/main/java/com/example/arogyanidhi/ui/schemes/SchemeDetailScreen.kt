package com.example.arogyanidhi.ui.schemes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchemeDetailScreen(schemeId: String, viewModel: SchemeDetailViewModel, onNavigateBack: () -> Unit) {
    val scheme by viewModel.scheme.collectAsState()
    val documents by viewModel.documents.collectAsState()

    LaunchedEffect(schemeId) { viewModel.loadScheme(schemeId) }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(scheme?.name ?: "Scheme Details") },
            navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }
        )
    }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            scheme?.let { s ->
                item {
                    AssistChip(onClick = {}, label = { Text(s.category) })
                    Spacer(Modifier.height(4.dp))
                    Text(s.description, style = MaterialTheme.typography.bodyLarge)
                }
                item {
                    Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Eligibility Criteria", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                            Spacer(Modifier.height(4.dp))
                            Text(s.eligibilityCriteria, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                    }
                }
                item {
                    Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Benefits", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSecondaryContainer)
                            Spacer(Modifier.height(4.dp))
                            Text(s.benefits, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSecondaryContainer)
                        }
                    }
                }
                item {
                    Text("Document Checklist", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Tap to mark documents you have ready", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                }
                items(documents) { doc ->
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { viewModel.toggleDocument(doc) }) {
                            Icon(
                                if (doc.isReady) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                null,
                                tint = if (doc.isReady) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                            )
                        }
                        Text(doc.name, style = MaterialTheme.typography.bodyMedium,
                            color = if (doc.isReady) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
                    }
                    HorizontalDivider(thickness = 0.5.dp)
                }
                if (documents.isNotEmpty()) {
                    item {
                        val ready = documents.count { it.isReady }
                        LinearProgressIndicator(progress = { ready.toFloat() / documents.size }, modifier = Modifier.fillMaxWidth())
                        Text("$ready / ${documents.size} documents ready", style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }
        }
    }
}
