package com.goomer.ps.di

import com.goomer.ps.data.datasource.LocalCardapioDataSource
import com.goomer.ps.data.repository.CardapioRepositoryImpl
import com.goomer.ps.domain.repository.CardapioRepository
import com.goomer.ps.domain.usecase.GetMenuItemsUseCase
import com.goomer.ps.presentation.viewmodel.MenuListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

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

    factory {
        GetMenuItemsUseCase(
            repository = get()
        )
    }

    viewModel {
        MenuListViewModel(
            getMenuItemsUseCase = get(),
            savedStateHandle = get()
        )
    }
}

