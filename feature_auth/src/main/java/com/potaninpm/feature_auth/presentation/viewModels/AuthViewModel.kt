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
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            val token = authRepository.getToken()
            _authState.value = if (!token.isNullOrEmpty()) {
                AuthState.Authorized
            } else {
                AuthState.Unauthorized
            }
        }
    }

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
            handleAuthAction(isLoginAttempt = true) {
                authRepository.signInWithYandex(token)
            }
        }
    }

    private suspend fun handleAuthAction(isLoginAttempt: Boolean, action: suspend () -> Boolean) {
        _authState.value = AuthState.Loading

        try {
            val success = action()
            _authState.value = if (success) AuthState.Authorized else AuthState.Error("Authentication failed", isLoginError = isLoginAttempt)
        } catch (e: Exception) {
            Log.e(TAG, "Authentication error", e)
            
            val errorMessage = when {
                e.message?.contains("422") == true || e.message?.contains("Invalid credentials") == true -> 
                    "Неверный email или пароль"
                e.message?.contains("409") == true || e.message?.contains("already exists") == true -> 
                    "Пользователь с таким email уже существует"
                e.message?.contains("404") == true -> 
                    "Сервер недоступен"
                e.message?.contains("timeout") == true || e.message?.contains("Unable to resolve host") == true -> 
                    "Ошибка сети. Проверьте подключение"
                else -> "Произошла ошибка. Попробуйте позже"
            }
            
            _authState.value = AuthState.Error(errorMessage, isLoginError = isLoginAttempt)
        }
    }

    fun logout() {
        authRepository.clearToken()
        _authState.value = AuthState.Unauthorized
    }


}
