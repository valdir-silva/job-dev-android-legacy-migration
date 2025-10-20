package com.goomer.ps.di

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.google.gson.Gson
import com.goomer.ps.domain.repository.CardapioRepository
import com.goomer.ps.domain.usecase.GetMenuItemByIdUseCase
import com.goomer.ps.domain.usecase.GetMenuItemsUseCase
import com.goomer.ps.presentation.viewmodel.MenuListViewModel
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class KoinModulesTest : KoinTest {

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockSavedStateHandle: SavedStateHandle

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `verifica que todos os módulos Koin estão configurados corretamente`() {
        // Inicializar Koin com todos os módulos e mocks
        startKoin {
            androidContext(mockContext)
            modules(
                appModule,
                dataModule,
                domainModule,
                presentationModule,

                module {
                    factory { mockSavedStateHandle }
                }
            )
        }

        // Verificar que cada dependência pode ser injetada
        val gson: Gson by inject()
        val repository: CardapioRepository by inject()
        val getMenuItemsUseCase: GetMenuItemsUseCase by inject()
        val getMenuItemByIdUseCase: GetMenuItemByIdUseCase by inject()
        val viewModel: MenuListViewModel by inject()

        // Verificar que as instâncias não são null
        assert(gson != null)
        assert(repository != null)
        assert(getMenuItemsUseCase != null)
        assert(getMenuItemByIdUseCase != null)
        assert(viewModel != null)
    }

    @Test
    fun `verifica que o dataModule está configurado corretamente`() {
        startKoin {
            androidContext(mockContext)
            modules(dataModule)
        }

        val repository: CardapioRepository by inject()
        assert(repository != null)
    }

    @Test
    fun `verifica que o domainModule está configurado corretamente`() {
        startKoin {
            androidContext(mockContext)
            modules(dataModule, domainModule)
        }

        val getMenuItemsUseCase: GetMenuItemsUseCase by inject()
        val getMenuItemByIdUseCase: GetMenuItemByIdUseCase by inject()
        
        assert(getMenuItemsUseCase != null)
        assert(getMenuItemByIdUseCase != null)
    }

    @Test
    fun `verifica que o presentationModule está configurado corretamente`() {
        startKoin {
            androidContext(mockContext)
            modules(
                dataModule, 
                domainModule, 
                presentationModule,

                module {
                    factory { mockSavedStateHandle }
                }
            )
        }

        val viewModel: MenuListViewModel by inject()
        assert(viewModel != null)
    }

    @Test
    fun `verifica que o appModule está configurado corretamente`() {
        startKoin {
            modules(appModule)
        }

        val gson: Gson by inject()
        assert(gson != null)
    }
}

