package com.example.arogyanidhi.ui.eligibility

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EligibilityScreen(
    viewModel: EligibilityViewModel,
    onNavigateBack: () -> Unit
) {
    val eligibilityData by viewModel.eligibilityData.collectAsState()
    val eligibleSchemes by viewModel.eligibleSchemes.collectAsState()
    var currentStep by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Eligibility Checker") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (eligibleSchemes.isEmpty()) {
                when (currentStep) {
                    0 -> {
                        Text("Step 1: Financial Information", style = MaterialTheme.typography.titleLarge)
                        OutlinedTextField(
                            value = eligibilityData.income.toString(),
                            onValueChange = { viewModel.updateIncome(it.toDoubleOrNull() ?: 0.0) },
                            label = { Text("Annual Income") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Checkbox(checked = eligibilityData.isBpl, onCheckedChange = { viewModel.updateBpl(it) })
                            Text("BPL Status")
                        }
                        Button(onClick = { currentStep = 1 }) { Text("Next") }
                    }
                    1 -> {
                        Text("Step 2: Professional Information", style = MaterialTheme.typography.titleLarge)
                        OutlinedTextField(
                            value = eligibilityData.occupation,
                            onValueChange = { viewModel.updateOccupation(it) },
                            label = { Text("Occupation") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button(onClick = { 
                            viewModel.checkEligibility()
                        }) { Text("Check Eligibility") }
                    }
                }
            } else {
                Text("Eligible Schemes", style = MaterialTheme.typography.titleLarge)
                LazyColumn {
                    items(eligibleSchemes) { scheme ->
                        Card(modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = scheme.name, style = MaterialTheme.typography.titleMedium)
                                Text(text = scheme.description)
                            }
                        }
                    }
                }
                Button(onClick = { 
                    // Reset or go back
                    onNavigateBack()
                }) { Text("Finish") }
            }
        }
    }
}
