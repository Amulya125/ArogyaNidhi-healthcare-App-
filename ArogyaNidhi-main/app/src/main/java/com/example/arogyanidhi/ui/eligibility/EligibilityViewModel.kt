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

    fun updateIncome(income: Double) {
        _eligibilityData.value = _eligibilityData.value.copy(income = income)
    }

    fun updateOccupation(occupation: String) {
        _eligibilityData.value = _eligibilityData.value.copy(occupation = occupation)
    }

    fun updateBpl(isBpl: Boolean) {
        _eligibilityData.value = _eligibilityData.value.copy(isBpl = isBpl)
    }

    fun updateFamilySize(size: Int) {
        _eligibilityData.value = _eligibilityData.value.copy(familySize = size)
    }

    fun checkEligibility() {
        viewModelScope.launch {
            // Decision Tree Logic
            val data = _eligibilityData.value
            val schemes = mutableListOf<Scheme>()
            
            if (data.income < 250000 || data.isBpl) {
                schemes.add(Scheme(id = "1", name = "Ayushman Bharat", description = "Free health insurance up to 5 Lakhs"))
            }
            
            if (data.occupation == "Farmer") {
                schemes.add(Scheme(id = "2", name = "PM Kisan Maandhan Yojana", description = "Pension scheme for farmers"))
            }
            
            _eligibleSchemes.value = schemes
        }
    }
}
