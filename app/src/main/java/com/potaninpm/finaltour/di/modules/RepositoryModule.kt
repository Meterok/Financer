package com.potaninpm.finaltour.di.modules


import com.potaninpm.feature_feed.data.repository.PostRepository
import com.potaninpm.feature_finances.data.repository.GoalsRepository
import com.potaninpm.feature_finances.data.repository.OperationsRepository
import com.potaninpm.feature_home.data.repository.NewsRepositoryImpl
import com.potaninpm.feature_home.data.repository.TickerRepositoryImpl
import com.potaninpm.feature_home.domain.repository.NewsRepository
import com.potaninpm.feature_home.domain.repository.TickerRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<TickerRepository> { TickerRepositoryImpl(get(), get()) }
    single<NewsRepository> { NewsRepositoryImpl(get(), get(), get()) }

    single<GoalsRepository> { GoalsRepository(get()) }
    single<OperationsRepository> { OperationsRepository(get()) }

    single<PostRepository> { PostRepository(get()) }
}