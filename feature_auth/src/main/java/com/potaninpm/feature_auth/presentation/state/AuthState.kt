package com.potaninpm.feature_auth.presentation.state

sealed class AuthState {
    data object Loading : AuthState()
    data object Authorized : AuthState()
    data object Unauthorized : AuthState()
    data class Error(val message: String, val isLoginError: Boolean = false) : AuthState()
}
