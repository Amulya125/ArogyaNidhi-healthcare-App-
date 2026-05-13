package com.example.arogyanidhi.ui.schemes

import androidx.lifecycle.ViewModel
import com.example.arogyanidhi.domain.model.Scheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SchemeViewModel @Inject constructor() : ViewModel() {
    private val _schemes = MutableStateFlow<List<Scheme>>(SchemeRepository.allSchemes)
    val schemes: StateFlow<List<Scheme>> = _schemes.asStateFlow()
}
