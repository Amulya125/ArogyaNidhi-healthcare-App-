package com.example.arogyanidhi.ui.schemes

import com.example.arogyanidhi.domain.model.Scheme

object SchemeRepository {

    val allSchemes = listOf(

        Scheme(
            id = "1",
            name = "Ayushman Bharat (PM-JAY)",
            description = "Free health insurance coverage up to ₹5 lakh per family annually.",
            eligibilityCriteria = "Families listed in SECC 2011 database and low-income households.",
            benefits = "Cashless treatment at empanelled hospitals across India.",
            documentsRequired = listOf(
                "Aadhaar Card",
                "Ration Card",
                "Income Certificate",
                "Family ID"
            ),
            category = "Health Insurance"
        ),

        Scheme(
            id = "2",
            name = "Janani Suraksha Yojana (JSY)",
            description = "Financial assistance for pregnant women for safe institutional delivery.",
            eligibilityCriteria = "Pregnant women from low-income families.",
            benefits = "Cash assistance and free delivery services in government hospitals.",
            documentsRequired = listOf(
                "Aadhaar Card",
                "Maternity Card",
                "Bank Passbook"
            ),
            category = "Maternal Health"
        ),

        Scheme(
            id = "3",
            name = "Rashtriya Swasthya Bima Yojana",
            description = "Health insurance scheme for below poverty line families.",
            eligibilityCriteria = "BPL families registered under the scheme.",
            benefits = "Hospitalization coverage for eligible families.",
            documentsRequired = listOf(
                "BPL Card",
                "Aadhaar Card",
                "Family Photo"
            ),
            category = "Health Insurance"
        ),

        Scheme(
            id = "4",
            name = "Arogya Karnataka",
            description = "Cashless healthcare support for Karnataka residents.",
            eligibilityCriteria = "Residents of Karnataka state.",
            benefits = "Free treatment in government and selected private hospitals.",
            documentsRequired = listOf(
                "Aadhaar Card",
                "Voter ID",
                "Ration Card"
            ),
            category = "State Health Scheme"
        ),

        Scheme(
            id = "5",
            name = "PM National Dialysis Programme",
            description = "Provides free dialysis services in district hospitals.",
            eligibilityCriteria = "Patients diagnosed with kidney failure requiring dialysis.",
            benefits = "Free dialysis treatment at government hospitals.",
            documentsRequired = listOf(
                "Doctor Prescription",
                "Aadhaar Card",
                "Hospital Records"
            ),
            category = "Critical Care"
        ),

        Scheme(
            id = "6",
            name = "Mission Indradhanush",
            description = "Vaccination programme for children and pregnant women.",
            eligibilityCriteria = "Children under 2 years and pregnant women.",
            benefits = "Free immunization services.",
            documentsRequired = listOf(
                "Child Birth Certificate",
                "Vaccination Card",
                "Aadhaar Card"
            ),
            category = "Child Healthcare"
        ),

        Scheme(
            id = "7",
            name = "PM Suraksha Bima Yojana",
            description = "Affordable accident insurance scheme.",
            eligibilityCriteria = "Citizens aged 18 to 70 years with bank account.",
            benefits = "Accidental death and disability insurance coverage.",
            documentsRequired = listOf(
                "Aadhaar Card",
                "Bank Passbook"
            ),
            category = "Insurance"
        )

    )

    fun getSchemeById(id: String): Scheme? {
        return allSchemes.find { it.id == id }
    }
}