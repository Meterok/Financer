package com.potaninpm.feature_auth.di

import android.content.Context
import android.content.SharedPreferences
import com.potaninpm.feature_auth.data.remote.api.AuthApi
import com.potaninpm.feature_auth.data.remote.api.interceptors.AuthInterceptor
import com.potaninpm.feature_auth.data.repository.AuthRepositoryImpl
import com.potaninpm.feature_auth.domain.repository.AuthRepository
import com.potaninpm.feature_auth.presentation.viewModels.AuthViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val authModule = module {

    single<SharedPreferences> {
        androidApplication().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    }

    single<AuthInterceptor> {
        AuthInterceptor(get())
    }

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(get<AuthInterceptor>())
            .build()
    }

    single<AuthApi> {
        Retrofit.Builder()
            .baseUrl("https://494410f51101.ngrok-free.app/api/")
            .client(get<OkHttpClient>())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }

    single<AuthRepository> {
        AuthRepositoryImpl(get(), get(), androidApplication())
    }

    viewModel { AuthViewModel() }
}

