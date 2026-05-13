package com.example.arogyanidhi.ui.eligibility

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EligibilityScreen(
    viewModel: EligibilityViewModel,
    onNavigateBack: () -> Unit
) {

    val data by viewModel.eligibilityData.collectAsState()
    val schemes by viewModel.eligibleSchemes.collectAsState()

    var step by remember { mutableStateOf(0) }
    var showError by remember { mutableStateOf(false) }

    val steps = listOf(
        "Income",
        "BPL Status",
        "Occupation",
        "Family",
        "Age"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Eligibility Checker") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
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

            if (schemes.isEmpty()) {

                LinearProgressIndicator(
                    progress = { (step + 1) / 5f },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Step ${step + 1} of 5: ${steps[step]}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(24.dp))

                AnimatedContent(
                    targetState = step,
                    label = "step"
                ) { currentStep ->

                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        when (currentStep) {

                            0 -> {

                                Text(
                                    "What is your annual family income?",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )

                                var incomeText by remember {
                                    mutableStateOf(
                                        if (data.income > 0)
                                            data.income.toInt().toString()
                                        else ""
                                    )
                                }

                                OutlinedTextField(
                                    value = incomeText,
                                    onValueChange = {
                                        incomeText = it
                                        viewModel.updateIncome(
                                            it.toDoubleOrNull() ?: 0.0
                                        )
                                    },
                                    label = {
                                        Text("Annual Income (₹)")
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Text(
                                    "Example: Enter 150000 for ₹1,50,000",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }

                            1 -> {

                                Text(
                                    "Do you have a BPL card?",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )

                                listOf(
                                    true to "Yes",
                                    false to "No"
                                ).forEach { (value, label) ->

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        RadioButton(
                                            selected = data.isBpl == value,
                                            onClick = {
                                                viewModel.updateBpl(value)
                                            }
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Text(label)
                                    }
                                }
                            }

                            2 -> {

                                Text(
                                    "What is your occupation?",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )

                                listOf(
                                    "Farmer",
                                    "Daily Labour",
                                    "Self-Employed / Business",
                                    "Salaried Employee",
                                    "Housewife",
                                    "Unemployed"
                                ).forEach { occupation ->

                                    FilterChip(
                                        selected = data.occupation == occupation,
                                        onClick = {
                                            viewModel.updateOccupation(occupation)
                                        },
                                        label = {
                                            Text(occupation)
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            3 -> {

                                Text(
                                    "How many members are in your family?",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )

                                var familyText by remember {
                                    mutableStateOf(
                                        if (data.familySize > 0)
                                            data.familySize.toString()
                                        else ""
                                    )
                                }

                                OutlinedTextField(
                                    value = familyText,
                                    onValueChange = {
                                        familyText = it
                                        viewModel.updateFamilySize(
                                            it.toIntOrNull() ?: 0
                                        )
                                    },
                                    label = {
                                        Text("Family Members")
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            4 -> {

                                Text(
                                    "What is your age?",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )

                                var ageText by remember {
                                    mutableStateOf(
                                        if (data.age > 0)
                                            data.age.toString()
                                        else ""
                                    )
                                }

                                OutlinedTextField(
                                    value = ageText,
                                    onValueChange = {
                                        ageText = it
                                        viewModel.updateAge(
                                            it.toIntOrNull() ?: 0
                                        )
                                    },
                                    label = {
                                        Text("Age")
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }

                if (showError) {

                    Spacer(modifier = Modifier.height(8.dp))

                    val errorText = when (step) {
                        0 -> "Please enter annual income"
                        2 -> "Please select occupation"
                        3 -> "Please enter family size"
                        4 -> "Please enter age"
                        else -> "Please complete this step"
                    }

                    Text(
                        text = errorText,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    if (step > 0) {

                        OutlinedButton(
                            onClick = {
                                step--
                                showError = false
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Back")
                        }
                    }

                    Button(
                        onClick = {

                            when (step) {

                                0 -> {
                                    if (data.income <= 0) {
                                        showError = true
                                        return@Button
                                    }
                                }

                                2 -> {
                                    if (data.occupation.isBlank()) {
                                        showError = true
                                        return@Button
                                    }
                                }

                                3 -> {
                                    if (data.familySize <= 0) {
                                        showError = true
                                        return@Button
                                    }
                                }

                                4 -> {
                                    if (data.age <= 0) {
                                        showError = true
                                        return@Button
                                    }
                                }
                            }

                            showError = false

                            if (step < 4) {
                                step++
                            } else {
                                viewModel.checkEligibility()
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            if (step < 4)
                                "Next"
                            else
                                "Check Eligibility"
                        )
                    }
                }

            } else {

                Text(
                    text = "You are eligible for ${schemes.size} scheme(s)!",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(schemes) { scheme ->

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {

                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Text(
                                        text = scheme.name,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(scheme.description)

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Benefits: ${scheme.benefits}",
                                    style = MaterialTheme.typography.bodySmall
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    "Documents Needed:",
                                    fontWeight = FontWeight.Bold
                                )

                                scheme.documentsRequired.forEach {

                                    Text("• $it")
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = {
                        viewModel.reset()
                        step = 0
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Check Again")
                }
            }
        }
    }
}