package com.goomer.ps.di

import com.goomer.ps.domain.usecase.GetMenuItemByIdUseCase
import com.goomer.ps.domain.usecase.GetMenuItemsUseCase
import org.koin.dsl.module

val domainModule =
    module {
        factory {
            GetMenuItemsUseCase(
                repository = get(),
            )
        }

        factory {
            GetMenuItemByIdUseCase(
                repository = get(),
            )
        }
    }
