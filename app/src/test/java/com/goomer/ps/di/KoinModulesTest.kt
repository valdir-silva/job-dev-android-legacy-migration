package com.goomer.ps.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.google.gson.Gson
import com.goomer.ps.data.datasource.LocalCardapioDataSource
import com.goomer.ps.domain.repository.CardapioRepository
import com.goomer.ps.domain.usecase.GetMenuItemByIdUseCase
import com.goomer.ps.domain.usecase.GetMenuItemsUseCase
import com.goomer.ps.presentation.viewmodel.MenuListViewModel
import org.junit.After
import org.junit.Assert.assertNotNull
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
    private lateinit var mockApplication: Application

    @Mock
    private lateinit var mockSavedStateHandle: SavedStateHandle

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        // Configurar mock do Application para retornar strings
        org.mockito.kotlin
            .whenever(mockApplication.getString(org.mockito.kotlin.any()))
            .thenReturn("mock_string")
        org.mockito.kotlin
            .whenever(mockApplication.getString(org.mockito.kotlin.any(), org.mockito.kotlin.any()))
            .thenReturn("mock_string")
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
                    single { mockApplication }
                    factory { mockSavedStateHandle }
                },
            )
        }

        // Verificar que cada dependência pode ser injetada
        val gson: Gson by inject()
        val repository: CardapioRepository by inject()
        val getMenuItemsUseCase: GetMenuItemsUseCase by inject()
        val getMenuItemByIdUseCase: GetMenuItemByIdUseCase by inject()
        val viewModel: MenuListViewModel by inject()

        // Verificar que as instâncias não são null e o grafo está completo
        assertNotNull(gson)
        assertNotNull(repository)
        assertNotNull(getMenuItemsUseCase)
        assertNotNull(getMenuItemByIdUseCase)
        assertNotNull(viewModel)

        // Verificar que as dependências foram resolvidas corretamente
        assertNotNull(repository)
        assertNotNull(getMenuItemsUseCase)
    }

    @Test
    fun `verifica que o dataModule está configurado corretamente`() {
        startKoin {
            androidContext(mockContext)
            modules(dataModule)
        }

        val repository: CardapioRepository by inject()
        val localDataSource: LocalCardapioDataSource by inject()

        assertNotNull(repository)
        assertNotNull(localDataSource)
    }

    @Test
    fun `verifica que o domainModule está configurado corretamente`() {
        startKoin {
            androidContext(mockContext)
            modules(dataModule, domainModule)
        }

        val getMenuItemsUseCase: GetMenuItemsUseCase by inject()
        val getMenuItemByIdUseCase: GetMenuItemByIdUseCase by inject()
        val repository: CardapioRepository by inject()

        assertNotNull(getMenuItemsUseCase)
        assertNotNull(getMenuItemByIdUseCase)
        assertNotNull(repository)
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
                    single { mockApplication }
                    factory { mockSavedStateHandle }
                },
            )
        }

        val viewModel: MenuListViewModel by inject()
        val useCase: GetMenuItemsUseCase by inject()

        assertNotNull(viewModel)
        assertNotNull(useCase)
    }

    @Test
    fun `verifica que o appModule está configurado corretamente`() {
        startKoin {
            modules(appModule)
        }

        val gson: Gson by inject()

        assertNotNull(gson)

        // Verificar que Gson está configurado corretamente
        val testJson = """{"test": "value"}"""
        val parsed = gson.fromJson(testJson, Map::class.java)
        assertNotNull(parsed)
    }
}
