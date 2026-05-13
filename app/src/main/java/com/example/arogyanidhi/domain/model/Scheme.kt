package com.example.arogyanidhi.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Scheme(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val eligibilityCriteria: String = "",
    val documentsRequired: List<String> = emptyList(),
    val benefits: String = "",
    val applicationProcess: String = "",
    val category: String = ""
)
