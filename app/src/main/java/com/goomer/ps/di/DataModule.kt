package com.goomer.ps.di

import com.goomer.ps.data.datasource.LocalCardapioDataSource
import com.goomer.ps.data.repository.CardapioRepositoryImpl
import com.goomer.ps.domain.repository.CardapioRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    single {
        LocalCardapioDataSource(
            context = androidContext()
        )
    }

    single<CardapioRepository> {
        CardapioRepositoryImpl(
            localDataSource = get()
        )
    }
}
