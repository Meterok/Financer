package com.potaninpm.feature_auth.presentation.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.potaninpm.feature_auth.R
import com.potaninpm.feature_auth.presentation.components.AuthentificationDivider
import com.potaninpm.feature_auth.presentation.components.BigButton
import com.potaninpm.feature_auth.presentation.components.InputField
import com.potaninpm.feature_auth.presentation.components.YandexSignInButton
import com.potaninpm.feature_auth.presentation.handleYandexAuthResult
import com.potaninpm.feature_auth.presentation.state.AuthState
import com.potaninpm.feature_auth.presentation.viewModels.AuthViewModel
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import org.koin.androidx.compose.koinViewModel

@Composable
fun WelcomeScreen(
    onAuthSuccess: () -> Unit
) {
    val authViewModel: AuthViewModel = koinViewModel()
    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val yandexSdk = YandexAuthSdk.create(YandexAuthOptions(context))
    val yandexAuthLauncher = rememberLauncherForActivityResult(contract = yandexSdk.contract) { result ->
        handleYandexAuthResult(result)?.let { token ->
            authViewModel.signInWithYandex(token)
        }
    }

    val onSignInWithYandexClick: () -> Unit = {
        val loginOptions = YandexAuthLoginOptions()
        yandexAuthLauncher.launch(loginOptions)
    }

    LaunchedEffect(authState) {
        if (authState is AuthState.Error) {
            val errorMessage = (authState as AuthState.Error).message
            snackbarHostState.showSnackbar(errorMessage)
        }
    }

    when (authState) {
        is AuthState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is AuthState.Authorized -> {
            onAuthSuccess()
        }
        else -> {
            WelcomeScreenContent(
                snackbarHostState = snackbarHostState,
                onSignInWithYandexClick = onSignInWithYandexClick,
                onLoginClick = { email, password ->
                    authViewModel.login(email, password)
                },
                onRegisterClick = { email, password ->
                    authViewModel.register(email, password, "", null)
                }
            )
        }
    }
}

@Composable
private fun WelcomeScreenContent(
    snackbarHostState: SnackbarHostState,
    onSignInWithYandexClick: () -> Unit = {},
    onLoginClick: (email: String, password: String) -> Unit = { _, _ -> },
    onRegisterClick: (email: String, password: String) -> Unit = { _, _ -> }
) {
    var isLoginMode by rememberSaveable { mutableStateOf(true) }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }

    val isButtonEnabled = email.isNotEmpty() && password.isNotEmpty() &&
            emailError == null && passwordError == null

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ElevatedCard(
                elevation = CardDefaults.elevatedCardElevation(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = null,
                        modifier = Modifier
                            .size(128.dp)
                            .clip(MaterialTheme.shapes.extraLarge)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = if (isLoginMode) stringResource(R.string.welcome_back) 
                               else stringResource(R.string.create_account),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isLoginMode) stringResource(R.string.enter_account)
                               else stringResource(R.string.create_account_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    InputField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = when {
                                it.isEmpty() -> context.getString(R.string.email_cannot_be_empty)
                                !it.contains("@") -> context.getString(R.string.invalid_email_format)
                                else -> null
                            }
                        },
                        label = stringResource(R.string.email),
                        error = emailError,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    InputField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = when {
                                it.isEmpty() -> context.getString(R.string.password_cannot_be_empty)
                                it.length < 8 -> context.getString(R.string.password_must_be_at_least_8_characters)
                                else -> null
                            }
                        },
                        label = stringResource(R.string.password),
                        error = passwordError,
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    BigButton(
                        onClick = {
                            if (isLoginMode) {
                                onLoginClick(email, password)
                            } else {
                                onRegisterClick(email, password)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = isButtonEnabled
                    ) {
                        Text(
                            if (isLoginMode) stringResource(R.string.sign_in)
                            else stringResource(R.string.create_account)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            AuthentificationDivider()

            Spacer(modifier = Modifier.height(16.dp))

            YandexSignInButton(onClick = onSignInWithYandexClick)

            Spacer(modifier = Modifier.height(24.dp))

            androidx.compose.material3.TextButton(
                onClick = { isLoginMode = !isLoginMode }
            ) {
                Text(
                    text = if (isLoginMode) stringResource(R.string.no_account_register)
                           else stringResource(R.string.have_account_login),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        }
    }
}