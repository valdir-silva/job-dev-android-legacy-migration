package com.goomer.ps.di

import com.goomer.ps.presentation.viewmodel.MenuListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule =
    module {
        viewModel {
            MenuListViewModel(
                getMenuItemsUseCase = get(),
                savedStateHandle = get(),
                application = androidApplication(),
            )
        }
    }
