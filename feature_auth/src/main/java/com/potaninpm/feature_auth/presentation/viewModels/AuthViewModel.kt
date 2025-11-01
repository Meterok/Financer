package com.potaninpm.feature_auth.presentation.viewModels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potaninpm.feature_auth.presentation.state.AuthState
import com.potaninpm.feature_auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private const val TAG = "INFOGALL"

class AuthViewModel: ViewModel(), KoinComponent {
    private val authRepository: AuthRepository by inject()
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthorized)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            handleAuthAction(isLoginAttempt = true) {
                authRepository.login(email, password)
            }
        }
    }

    fun register(email: String, password: String, fullName: String, avatarUri: Uri?) {
        viewModelScope.launch {
            handleAuthAction(isLoginAttempt = false) {
                authRepository.register(email, password, fullName, avatarUri)
            }
        }
    }

    fun signInWithYandex(token: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            kotlinx.coroutines.delay(500)
            _authState.value = AuthState.Authorized
        }
    }

    private suspend fun handleAuthAction(isLoginAttempt: Boolean, action: suspend () -> Boolean) {
        _authState.value = AuthState.Loading

        try {
            val success = action()
            _authState.value = if (success) AuthState.Authorized else AuthState.Error("Authentication failed", isLoginError = isLoginAttempt)
        } catch (e: Exception) {
            Log.e(TAG, "Authentication error", e)
            _authState.value = AuthState.Error(e.message ?: "An unexpected error occurred", isLoginError = isLoginAttempt)
        }
    }

    fun logout() {
        authRepository.clearToken()
        _authState.value = AuthState.Unauthorized
    }

    fun testLogin(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            kotlinx.coroutines.delay(500)
            if (email.isNotEmpty() && password.isNotEmpty()) {
                _authState.value = AuthState.Authorized
            } else {
                _authState.value = AuthState.Error("Заполните все поля", isLoginError = true)
            }
        }
    }
}
