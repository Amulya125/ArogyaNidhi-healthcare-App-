package com.example.arogyanidhi.ui.hospitals

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.arogyanidhi.domain.model.Hospital

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalListScreen(
    viewModel: HospitalViewModel,
    onNavigateBack: () -> Unit
) {

    val hospitals by viewModel.hospitals.collectAsState()

    val isLocationEnabled by viewModel.isLocationEnabled.collectAsState()

    val selectedDistrict by viewModel.searchDistrict.collectAsState()

    val context = LocalContext.current

    val districts = listOf(

        "All Districts",

        "Bagalkote",
        "Ballari",
        "Belagavi",
        "Bangalore Rural",
        "Bangalore Urban",
        "Bidar",
        "Chamarajanagar",
        "Chikkaballapur",
        "Chikkamagaluru",
        "Chitradurga",
        "Dakshina Kannada",
        "Davanagere",
        "Dharwad",
        "Gadag",
        "Hassan",
        "Haveri",
        "Kalaburagi",
        "Kodagu",
        "Kolar",
        "Koppal",
        "Mandya",
        "Mysuru",
        "Raichur",
        "Ramanagara",
        "Shivamogga",
        "Tumakuru",
        "Udupi",
        "Uttara Kannada",
        "Vijayapura",
        "Yadgir"
    )

    var expanded by remember {
        mutableStateOf(false)
    }

    val locationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val granted =
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                        permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (granted) {

                viewModel.toggleLocation(true)

            } else {

                viewModel.toggleLocation(false)
            }
        }

    Scaffold(
        topBar = {

            TopAppBar(
                title = {
                    Text("Empanelled Hospitals")
                },

                navigationIcon = {

                    IconButton(
                        onClick = onNavigateBack
                    ) {

                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },

                actions = {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text =
                                if (isLocationEnabled)
                                    "GPS On"
                                else
                                    "GPS Off",

                            style = MaterialTheme.typography.labelMedium,

                            color =
                                if (isLocationEnabled)
                                    MaterialTheme.colorScheme.primary
                                else
                                    Color.Gray
                        )

                        Switch(
                            checked = isLocationEnabled,

                            onCheckedChange = { enabled ->

                                if (enabled) {

                                    locationPermissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )

                                } else {

                                    viewModel.toggleLocation(false)
                                }
                            },

                            modifier = Modifier.scale(0.7f)
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
        ) {

            Text(
                text = "Search Hospitals by District",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 16.dp,
                    bottom = 8.dp
                )
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {

                OutlinedButton(
                    onClick = {
                        expanded = true
                    },

                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text =
                            if (selectedDistrict.isBlank())
                                "Select District"
                            else
                                selectedDistrict
                    )
                }

                DropdownMenu(
                    expanded = expanded,

                    onDismissRequest = {
                        expanded = false
                    }
                ) {

                    districts.forEach { district ->

                        DropdownMenuItem(
                            text = {
                                Text(district)
                            },

                            onClick = {

                                expanded = false

                                if (district == "All Districts") {

                                    viewModel.updateDistrictSearch("")

                                } else {

                                    viewModel.updateDistrictSearch(
                                        district
                                    )
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (isLocationEnabled) {

                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,

                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = "Showing nearby hospitals within 100 km",

                        modifier = Modifier.padding(8.dp),

                        style = MaterialTheme.typography.labelSmall,

                        color =
                            MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            if (hospitals.isEmpty()) {

                Box(
                    modifier = Modifier.fillMaxSize(),

                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "No hospitals found."
                    )
                }

            } else {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {

                    items(hospitals) { hospital ->

                        HospitalCard(
                            hospital = hospital,

                            distance =
                                viewModel.getDistance(hospital),

                            onNavigateClick = {

                                val uri = Uri.parse(
                                    "google.navigation:q=${hospital.latitude},${hospital.longitude}"
                                )

                                val intent =
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        uri
                                    )

                                intent.setPackage(
                                    "com.google.android.apps.maps"
                                )

                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HospitalCard(
    hospital: Hospital,
    distance: String,
    onNavigateClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween,

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text = hospital.name,

                    style =
                        MaterialTheme.typography.titleMedium,

                    fontWeight = FontWeight.Bold,

                    modifier = Modifier.weight(1f)
                )

                if (distance.isNotEmpty()) {

                    Surface(
                        color =
                            MaterialTheme.colorScheme.primaryContainer,

                        shape =
                            MaterialTheme.shapes.small
                    ) {

                        Text(
                            text = distance,

                            modifier = Modifier.padding(
                                horizontal = 8.dp,
                                vertical = 4.dp
                            ),

                            style =
                                MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text =
                    "${hospital.type} • ${hospital.district}",

                style =
                    MaterialTheme.typography.bodyMedium,

                color =
                    MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(
                    Icons.Default.LocationOn,

                    contentDescription = null,

                    modifier = Modifier.size(16.dp),

                    tint = Color.Gray
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = hospital.address,

                    style =
                        MaterialTheme.typography.bodySmall,

                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(
                    Icons.Default.Phone,

                    contentDescription = null,

                    modifier = Modifier.size(16.dp),

                    tint = Color.Gray
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = hospital.contact,

                    style =
                        MaterialTheme.typography.bodySmall,

                    color =
                        MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onNavigateClick,

                modifier =
                    Modifier.align(
                        Alignment.End
                    )
            ) {

                Icon(
                    Icons.Default.Directions,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text("Navigate")
            }
        }
    }
}