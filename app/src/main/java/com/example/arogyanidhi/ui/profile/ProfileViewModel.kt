package com.example.arogyanidhi.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arogyanidhi.domain.model.UserProfile
import com.example.arogyanidhi.domain.repository.AuthRepository
import com.example.arogyanidhi.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    init {
        viewModelScope.launch {

            authRepository.currentUser.collect { user ->

                Log.d("PROFILE_DEBUG", "Current User: ${user?.uid}")

                user?.let {

                    userRepository.getUserProfile(it.uid).collect { profile ->

                        Log.d("PROFILE_DEBUG", "Fetched Profile: $profile")

                        _userProfile.value = profile
                    }
                }
            }
        }
    }

    fun saveProfile(profile: UserProfile) {

        Log.d("PROFILE_DEBUG", "Save button clicked")
        Log.d("PROFILE_DEBUG", "Saving profile: $profile")

        viewModelScope.launch {

            val result = userRepository.saveUserProfile(profile)

            result.onSuccess {

                Log.d("PROFILE_DEBUG", "PROFILE SAVED SUCCESSFULLY")
            }

            result.onFailure {

                Log.e(
                    "PROFILE_DEBUG",
                    "SAVE FAILED: ${it.message}"
                )
            }
        }
    }
}