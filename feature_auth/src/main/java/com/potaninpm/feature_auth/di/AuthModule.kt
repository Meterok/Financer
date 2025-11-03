package com.potaninpm.feature_auth.di

import android.content.Context
import android.content.SharedPreferences
import com.potaninpm.core.ApiConstants
import com.potaninpm.feature_auth.data.remote.api.AuthApi
import com.potaninpm.feature_auth.data.remote.api.interceptors.AuthInterceptor
import com.potaninpm.feature_auth.data.repository.AuthRepositoryImpl
import com.potaninpm.feature_auth.domain.repository.AuthRepository
import com.potaninpm.feature_auth.presentation.viewModels.AuthViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(get<AuthInterceptor>())
            .build()
    }

    single<AuthApi> {
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
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

