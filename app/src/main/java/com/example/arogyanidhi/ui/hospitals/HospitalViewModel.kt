package com.example.arogyanidhi.ui.hospitals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arogyanidhi.domain.model.Hospital
import com.example.arogyanidhi.util.LocationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HospitalViewModel @Inject constructor(
    private val locationHelper: LocationHelper
) : ViewModel() {

    private val allHospitals = listOf(

        // Bangalore Urban
        Hospital(
            id = "G1",
            name = "Victoria Hospital",
            district = "Bangalore Urban",
            address = "Fort Road",
            contact = "080-26701150",
            type = "Govt",
            latitude = 12.9634,
            longitude = 77.5756
        ),

        Hospital(
            id = "G2",
            name = "Bowring Hospital",
            district = "Bangalore Urban",
            address = "Shivajinagar",
            contact = "080-25591362",
            type = "Govt",
            latitude = 12.9837,
            longitude = 77.6015
        ),

        Hospital(
            id = "P1",
            name = "Apollo Hospital",
            district = "Bangalore Urban",
            address = "Bannerghatta Road",
            contact = "080-26304050",
            type = "Private",
            latitude = 12.8953,
            longitude = 77.6010
        ),

        Hospital(
            id = "P2",
            name = "Fortis Hospital",
            district = "Bangalore Urban",
            address = "Bannerghatta Road",
            contact = "080-66214444",
            type = "Private",
            latitude = 12.8943,
            longitude = 77.5990
        ),
        Hospital(
            id = "BR1",
            name = "Nelamangala Government Hospital",
            district = "Bangalore Rural",
            address = "Nelamangala Town",
            contact = "080-27722001",
            type = "Govt",
            latitude = 13.0995,
            longitude = 77.3935
        ),

        Hospital(
            id = "BR2",
            name = "Devanahalli Government Hospital",
            district = "Bangalore Rural",
            address = "Devanahalli",
            contact = "080-27682001",
            type = "Govt",
            latitude = 13.2422,
            longitude = 77.7132
        ),

        Hospital(
            id = "BR3",
            name = "Doddaballapur District Hospital",
            district = "Bangalore Rural",
            address = "Doddaballapur",
            contact = "080-27622010",
            type = "Govt",
            latitude = 13.2948,
            longitude = 77.5378
        ),

        // Mysuru
        Hospital(
            id = "M1",
            name = "K.R. Hospital",
            district = "Mysuru",
            address = "Sayyaji Rao Road",
            contact = "0821-2520512",
            type = "Govt",
            latitude = 12.3052,
            longitude = 76.6552
        ),

        Hospital(
            id = "M2",
            name = "JSS Hospital",
            district = "Mysuru",
            address = "MG Road",
            contact = "0821-2548416",
            type = "Private",
            latitude = 12.2958,
            longitude = 76.6394
        ),

        // Tumakuru
        Hospital(
            id = "T1",
            name = "District Hospital Tumakuru",
            district = "Tumakuru",
            address = "BH Road",
            contact = "0816-2278455",
            type = "Govt",
            latitude = 13.3379,
            longitude = 77.1006
        ),

        // Dakshina Kannada
        Hospital(
            id = "DK1",
            name = "Wenlock Hospital",
            district = "Dakshina Kannada",
            address = "Hampankatta, Mangaluru",
            contact = "0824-2444590",
            type = "Govt",
            latitude = 12.8698,
            longitude = 74.8431
        ),

        Hospital(
            id = "DK2",
            name = "AJ Hospital",
            district = "Dakshina Kannada",
            address = "Kuntikana, Mangaluru",
            contact = "0824-6613255",
            type = "Private",
            latitude = 12.9025,
            longitude = 74.8550
        ),

        // Dharwad
        Hospital(
            id = "DH1",
            name = "KIMS Hospital",
            district = "Dharwad",
            address = "Vidyanagar, Hubballi",
            contact = "0836-2373348",
            type = "Govt",
            latitude = 15.3647,
            longitude = 75.1240
        ),

        // Belagavi
        Hospital(
            id = "B1",
            name = "BIMS Hospital",
            district = "Belagavi",
            address = "Civil Hospital Road",
            contact = "0831-2429200",
            type = "Govt",
            latitude = 15.8497,
            longitude = 74.4977
        ),

        // Kalaburagi
        Hospital(
            id = "K1",
            name = "District Hospital Kalaburagi",
            district = "Kalaburagi",
            address = "Sedam Road",
            contact = "08472-220998",
            type = "Govt",
            latitude = 17.3297,
            longitude = 76.8343
        ),

        // Shivamogga
        Hospital(
            id = "S1",
            name = "McGann Hospital",
            district = "Shivamogga",
            address = "Sagar Road",
            contact = "08182-271101",
            type = "Govt",
            latitude = 13.9299,
            longitude = 75.5681
        ),

        // Davanagere
        Hospital(
            id = "D1",
            name = "Chigateri Hospital",
            district = "Davanagere",
            address = "PJ Extension",
            contact = "08192-234061",
            type = "Govt",
            latitude = 14.4644,
            longitude = 75.9218
        ),

        // Ballari
        Hospital(
            id = "BL1",
            name = "VIMS Hospital",
            district = "Ballari",
            address = "Cantonment",
            contact = "08392-242424",
            type = "Govt",
            latitude = 15.1394,
            longitude = 76.9214
        ),

        // Udupi
        Hospital(
            id = "U1",
            name = "District Hospital Udupi",
            district = "Udupi",
            address = "Ajjarkad",
            contact = "0820-2522000",
            type = "Govt",
            latitude = 13.3409,
            longitude = 74.7421
        ),

        // Hassan
        Hospital(
            id = "HS1",
            name = "Hassan Institute of Medical Sciences",
            district = "Hassan",
            address = "Ring Road",
            contact = "08172-267345",
            type = "Govt",
            latitude = 13.0050,
            longitude = 76.1025
        ),

        // Chikkamagaluru
        Hospital(
            id = "C1",
            name = "District Hospital Chikkamagaluru",
            district = "Chikkamagaluru",
            address = "MG Road",
            contact = "08262-230101",
            type = "Govt",
            latitude = 13.3161,
            longitude = 75.7720
        ),

        // Raichur
        Hospital(
            id = "R1",
            name = "RIMS Hospital",
            district = "Raichur",
            address = "Hyderabad Road",
            contact = "08532-223344",
            type = "Govt",
            latitude = 16.2076,
            longitude = 77.3463
        ),

        // Vijayapura
        Hospital(
            id = "V1",
            name = "District Hospital Vijayapura",
            district = "Vijayapura",
            address = "Station Road",
            contact = "08352-250999",
            type = "Govt",
            latitude = 16.8302,
            longitude = 75.7100
        ),

        // Additional Karnataka Districts
        Hospital(
            id = "BG1",
            name = "District Hospital Bagalkote",
            district = "Bagalkote",
            address = "Main Road",
            contact = "08354-220001",
            type = "Govt",
            latitude = 16.1867,
            longitude = 75.6961
        ),


        Hospital(
            id = "BD1",
            name = "Bidar District Hospital",
            district = "Bidar",
            address = "Bidar City",
            contact = "08482-220101",
            type = "Govt",
            latitude = 17.9133,
            longitude = 77.5301
        ),

        Hospital(
            id = "CH1",
            name = "Chamarajanagar District Hospital",
            district = "Chamarajanagar",
            address = "MG Road",
            contact = "08226-220500",
            type = "Govt",
            latitude = 11.9231,
            longitude = 76.9395
        ),

        Hospital(
            id = "CB1",
            name = "Chikkaballapur District Hospital",
            district = "Chikkaballapur",
            address = "Hospital Road",
            contact = "08156-275001",
            type = "Govt",
            latitude = 13.4350,
            longitude = 77.7315
        ),

        Hospital(
            id = "CT1",
            name = "Chitradurga District Hospital",
            district = "Chitradurga",
            address = "Main Area",
            contact = "08194-230101",
            type = "Govt",
            latitude = 14.2306,
            longitude = 76.3980
        ),

        Hospital(
            id = "GD1",
            name = "Gadag Institute Hospital",
            district = "Gadag",
            address = "Station Road",
            contact = "08372-235001",
            type = "Govt",
            latitude = 15.4315,
            longitude = 75.6355
        ),

        Hospital(
            id = "HV1",
            name = "Haveri District Hospital",
            district = "Haveri",
            address = "PB Road",
            contact = "08375-220011",
            type = "Govt",
            latitude = 14.7935,
            longitude = 75.4041
        ),

        Hospital(
            id = "KD1",
            name = "Kodagu District Hospital",
            district = "Kodagu",
            address = "Madikeri",
            contact = "08272-220221",
            type = "Govt",
            latitude = 12.4244,
            longitude = 75.7382
        ),

        Hospital(
            id = "KL1",
            name = "Kolar District Hospital",
            district = "Kolar",
            address = "Kolar Main",
            contact = "08152-220101",
            type = "Govt",
            latitude = 13.1367,
            longitude = 78.1299
        ),

        Hospital(
            id = "KP1",
            name = "Koppal District Hospital",
            district = "Koppal",
            address = "Koppal Town",
            contact = "08539-220100",
            type = "Govt",
            latitude = 15.3483,
            longitude = 76.1548
        ),

        Hospital(
            id = "MD1",
            name = "Mandya District Hospital",
            district = "Mandya",
            address = "Mandya City",
            contact = "08232-220301",
            type = "Govt",
            latitude = 12.5239,
            longitude = 76.8953
        ),

        Hospital(
            id = "RM1",
            name = "Ramanagara District Hospital",
            district = "Ramanagara",
            address = "BM Road",
            contact = "080-27271224",
            type = "Govt",
            latitude = 12.7209,
            longitude = 77.2810
        ),

        Hospital(
            id = "UK1",
            name = "Karwar District Hospital",
            district = "Uttara Kannada",
            address = "Karwar",
            contact = "08382-220100",
            type = "Govt",
            latitude = 14.8136,
            longitude = 74.1297
        ),

        Hospital(
            id = "YG1",
            name = "Yadgir District Hospital",
            district = "Yadgir",
            address = "Yadgir Main",
            contact = "08473-220001",
            type = "Govt",
            latitude = 16.7704,
            longitude = 77.1376
        )
    )

    private val _userLocation =
        MutableStateFlow<android.location.Location?>(null)

    private val _isLocationEnabled =
        MutableStateFlow(false)

    val isLocationEnabled: StateFlow<Boolean> =
        _isLocationEnabled.asStateFlow()

    private val _searchDistrict =
        MutableStateFlow("")

    val searchDistrict: StateFlow<String> =
        _searchDistrict.asStateFlow()

    private val _hospitals =
        MutableStateFlow<List<Hospital>>(allHospitals)

    val hospitals: StateFlow<List<Hospital>> =
        _hospitals.asStateFlow()

    fun toggleLocation(enabled: Boolean) {

        _isLocationEnabled.value = enabled

        if (enabled) {

            startLocationUpdates()

        } else {

            applyFilters()

            _userLocation.value = null
        }
    }

    fun updateDistrictSearch(query: String) {

        _searchDistrict.value = query

        applyFilters()
    }

    private fun startLocationUpdates() {

        viewModelScope.launch {

            locationHelper.getLocationUpdates().collect { location ->

                if (_isLocationEnabled.value) {

                    _userLocation.value = location

                    applyFilters()
                }
            }
        }
    }

    private fun applyFilters() {

        var filtered = allHospitals

        val districtQuery = _searchDistrict.value

        if (districtQuery.isNotBlank()) {

            filtered = filtered.filter {

                it.district.contains(
                    districtQuery,
                    ignoreCase = true
                )
            }
        }

        val location = _userLocation.value

        if (_isLocationEnabled.value && location != null) {

            filtered = filtered.filter { hospital ->

                val distance =
                    locationHelper.calculateDistance(
                        location.latitude,
                        location.longitude,
                        hospital.latitude,
                        hospital.longitude
                    )

                distance <= 100.0
            }.sortedBy { hospital ->

                locationHelper.calculateDistance(
                    location.latitude,
                    location.longitude,
                    hospital.latitude,
                    hospital.longitude
                )
            }
        }

        _hospitals.value = filtered
    }

    fun getDistance(hospital: Hospital): String {

        val location = _userLocation.value ?: return ""

        val distance =
            locationHelper.calculateDistance(
                location.latitude,
                location.longitude,
                hospital.latitude,
                hospital.longitude
            )

        return String.format("%.1f km", distance)
    }
}