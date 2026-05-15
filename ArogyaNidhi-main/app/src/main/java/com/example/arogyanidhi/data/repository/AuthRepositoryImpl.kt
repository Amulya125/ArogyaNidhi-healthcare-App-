package com.example.arogyanidhi.data.repository

import com.example.arogyanidhi.domain.model.User
import com.example.arogyanidhi.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor() : AuthRepository {

    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUser: Flow<User?> = _currentUser.asStateFlow()

    override suspend fun login(email: String, password: String): Result<User> {
        val user = User(uid = "dummy_uid", email = email)
        _currentUser.value = user
        return Result.success(user)
    }

    override suspend fun register(email: String, password: String): Result<User> {
        val user = User(uid = "dummy_uid", email = email)
        _currentUser.value = user
        return Result.success(user)
    }

    override suspend fun logout() {
        _currentUser.value = null
    }

    override suspend fun forgotPassword(email: String): Result<Unit> {
        return Result.success(Unit)
    }
}
