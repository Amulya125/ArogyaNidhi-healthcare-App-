package com.example.arogyanidhi.ui.eligibility

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arogyanidhi.domain.model.EligibilityData
import com.example.arogyanidhi.domain.model.Scheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EligibilityViewModel @Inject constructor() : ViewModel() {

    private val _eligibilityData = MutableStateFlow(EligibilityData())
    val eligibilityData: StateFlow<EligibilityData> = _eligibilityData.asStateFlow()

    private val _eligibleSchemes = MutableStateFlow<List<Scheme>>(emptyList())
    val eligibleSchemes: StateFlow<List<Scheme>> = _eligibleSchemes.asStateFlow()

    fun updateIncome(income: Double) { _eligibilityData.value = _eligibilityData.value.copy(income = income) }
    fun updateOccupation(occupation: String) { _eligibilityData.value = _eligibilityData.value.copy(occupation = occupation) }
    fun updateBpl(isBpl: Boolean) { _eligibilityData.value = _eligibilityData.value.copy(isBpl = isBpl) }
    fun updateFamilySize(size: Int) { _eligibilityData.value = _eligibilityData.value.copy(familySize = size) }
    fun updateAge(age: Int) { _eligibilityData.value = _eligibilityData.value.copy(age = age) }

    fun checkEligibility() {
        viewModelScope.launch {
            val d = _eligibilityData.value
            val schemes = mutableListOf<Scheme>()

            // 1. Ayushman Bharat PM-JAY
            if (d.isBpl || d.income < 250000) {
                schemes.add(Scheme(id = "1", name = "Ayushman Bharat PM-JAY",
                    description = "Free health insurance up to ₹5 Lakhs per family per year at empaneled hospitals.",
                    eligibilityCriteria = "BPL families or annual income below ₹2.5 Lakh",
                    benefits = "Cashless treatment up to ₹5 Lakh at 24,000+ empaneled hospitals",
                    documentsRequired = listOf("Aadhaar Card","BPL Ration Card","Income Certificate","Family Photo"),
                    category = "Health"))
            }

            // 2. Rashtriya Swasthya Bima Yojana
            if (d.isBpl && d.income < 50000) {
                schemes.add(Scheme(id = "2", name = "Rashtriya Swasthya Bima Yojana",
                    description = "Health insurance for BPL families providing coverage up to ₹30,000.",
                    eligibilityCriteria = "BPL card holder with income below ₹50,000",
                    benefits = "Inpatient treatment up to ₹30,000 per family per year",
                    documentsRequired = listOf("BPL Card","Aadhaar Card","Bank Passbook"),
                    category = "Health"))
            }

            // 3. PM Jan Arogya Yojana
            if (d.familySize >= 3 && d.income < 250000) {
                schemes.add(Scheme(id = "3", name = "PM Jan Arogya Yojana",
                    description = "Larger families with lower income get enhanced hospitalization coverage.",
                    eligibilityCriteria = "Family of 3+ members with income below ₹2.5 Lakh",
                    benefits = "Hospitalization coverage across 1,393 procedures",
                    documentsRequired = listOf("Aadhaar Card","Income Certificate","Family Ration Card"),
                    category = "Health"))
            }

            // 4. Janani Suraksha Yojana (women)
            if (d.occupation.contains("housewife", ignoreCase = true) || d.occupation.contains("pregnant", ignoreCase = true)) {
                schemes.add(Scheme(id = "4", name = "Janani Suraksha Yojana",
                    description = "Cash assistance to pregnant women for institutional delivery.",
                    eligibilityCriteria = "Pregnant women delivering at government health centres",
                    benefits = "₹1,400 in rural areas, ₹1,000 in urban areas for institutional delivery",
                    documentsRequired = listOf("Aadhaar Card","MCH Card","Bank Account Passbook"),
                    category = "Maternal Health"))
            }

            // 5. PM Kisan Maandhan Yojana (farmers)
            if (d.occupation.contains("farmer", ignoreCase = true) || d.occupation.contains("agriculture", ignoreCase = true)) {
                schemes.add(Scheme(id = "5", name = "PM Kisan Maandhan Yojana",
                    description = "Pension scheme for small and marginal farmers.",
                    eligibilityCriteria = "Farmer with landholding up to 2 hectares, age 18-40",
                    benefits = "Monthly pension of ₹3,000 after age 60",
                    documentsRequired = listOf("Aadhaar Card","Land Records","Bank Passbook","PM-Kisan Registration"),
                    category = "Agriculture"))
            }

            // 6. PM Suraksha Bima Yojana (broad eligibility)
            if (d.age in 18..70 && d.income < 500000) {
                schemes.add(Scheme(id = "6", name = "PM Suraksha Bima Yojana",
                    description = "Accidental insurance scheme at just ₹20/year premium.",
                    eligibilityCriteria = "Age 18-70 with a savings bank account",
                    benefits = "₹2 Lakh coverage for accidental death, ₹1 Lakh for partial disability",
                    documentsRequired = listOf("Aadhaar Card","Bank Passbook"),
                    category = "Insurance"))
            }

            // 7. PM Jeevan Jyoti Bima Yojana
            if (d.age in 18..50 && d.income < 500000) {
                schemes.add(Scheme(id = "7", name = "PM Jeevan Jyoti Bima Yojana",
                    description = "Life insurance scheme at ₹436/year for any cause of death.",
                    eligibilityCriteria = "Age 18-50 with a savings bank account",
                    benefits = "₹2 Lakh life cover for death from any reason",
                    documentsRequired = listOf("Aadhaar Card","Bank Passbook"),
                    category = "Insurance"))
            }

            // 8. Atal Pension Yojana
            if (d.age in 18..40 && (d.occupation.contains("labour", ignoreCase = true) || d.occupation.contains("unorganised", ignoreCase = true) || d.income < 300000)) {
                schemes.add(Scheme(id = "8", name = "Atal Pension Yojana",
                    description = "Guaranteed pension for unorganised sector workers.",
                    eligibilityCriteria = "Age 18-40, not a govt employee, not income tax payer",
                    benefits = "Fixed pension ₹1,000-₹5,000/month after age 60",
                    documentsRequired = listOf("Aadhaar Card","Mobile Number","Bank Account"),
                    category = "Pension"))
            }

            // 9. Arogya Karnataka
            if (d.income < 500000) {
                schemes.add(Scheme(id = "9", name = "Arogya Karnataka",
                    description = "Karnataka state scheme providing free medical treatment.",
                    eligibilityCriteria = "Karnataka resident with annual income below ₹5 Lakh",
                    benefits = "Free OPD and IPD treatment at government hospitals; up to ₹5 Lakh for critical illnesses",
                    documentsRequired = listOf("Aadhaar Card","Karnataka Domicile Certificate","Income Certificate","Ration Card"),
                    category = "Health"))
            }

            // 10. PM Mudra Yojana (self-employed)
            if (d.occupation.contains("self", ignoreCase = true) || d.occupation.contains("business", ignoreCase = true) || d.occupation.contains("shop", ignoreCase = true)) {
                schemes.add(Scheme(id = "10", name = "PM Mudra Yojana",
                    description = "Loans for small and micro enterprises without collateral.",
                    eligibilityCriteria = "Non-corporate, non-farm small business owner",
                    benefits = "Loans up to ₹10 Lakh (Shishu ₹50k, Kishore ₹5L, Tarun ₹10L)",
                    documentsRequired = listOf("Aadhaar Card","Business Plan","Bank Statements","PAN Card"),
                    category = "Employment"))
            }

            _eligibleSchemes.value = schemes
        }
    }

    fun reset() {
        _eligibleSchemes.value = emptyList()
        _eligibilityData.value = EligibilityData()
    }
}
