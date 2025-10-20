package com.goomer.ps.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.goomer.ps.domain.model.CardapioResult
import com.goomer.ps.domain.model.MenuItem
import com.goomer.ps.domain.usecase.GetMenuItemsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class MenuListViewModelTest {

    private lateinit var mockUseCase: GetMenuItemsUseCase
    private lateinit var mockSavedStateHandle: SavedStateHandle
    private lateinit var viewModel: MenuListViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockUseCase = mock()
        mockSavedStateHandle = mock()
        viewModel = MenuListViewModel(mockUseCase, mockSavedStateHandle)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `quando carregar menu com sucesso, deve emitir Loading e depois Success`() = runTest {
        // Given
        val items = listOf(
            MenuItem(id = 1, name = "Pizza", description = "Deliciosa pizza", price = 29.90),
            MenuItem(id = 2, name = "Hamburguer", description = "Hamburguer artesanal", price = 19.90)
        )
        
        whenever(mockUseCase.invoke()).thenReturn(
            flowOf(
                CardapioResult.Loading(),
                CardapioResult.Success(items)
            )
        )
        
        // When
        viewModel.loadMenu()
        
        // Then
        viewModel.uiState.test {
            val loadingState = awaitItem()
            assertTrue(loadingState is CardapioResult.Loading, "Primeiro estado deve ser Loading")
            
            val successState = awaitItem()
            assertTrue(successState is CardapioResult.Success, "Segundo estado deve ser Success")
            assertEquals(items, successState.value)
        }
    }

    @Test
    fun `quando erro ao carregar menu, deve emitir Failure`() = runTest {
        // Given
        val errorMessage = "Erro ao carregar itens do menu"
        val exception = Exception(errorMessage)
        
        whenever(mockUseCase.invoke()).thenReturn(
            flowOf(
                CardapioResult.Loading(),
                CardapioResult.Failure(exception)
            )
        )
        
        // When
        viewModel.loadMenu()
        
        // Then
        viewModel.uiState.test {
            val loadingState = awaitItem()
            assertTrue(loadingState is CardapioResult.Loading, "Primeiro estado deve ser Loading")
            
            val errorState = awaitItem()
            assertTrue(errorState is CardapioResult.Failure, "Segundo estado deve ser Failure")
            assertEquals(errorMessage, errorState.throwable?.message)
        }
    }

    @Test
    fun `quando carregar menu vazio, deve emitir Success com lista vazia`() = runTest {
        // Given
        val emptyList = emptyList<MenuItem>()
        
        whenever(mockUseCase.invoke()).thenReturn(
            flowOf(
                CardapioResult.Loading(),
                CardapioResult.Success(emptyList)
            )
        )
        
        // When
        viewModel.loadMenu()
        
        // Then
        viewModel.uiState.test {
            val loadingState = awaitItem()
            assertTrue(loadingState is CardapioResult.Loading)
            
            val successState = awaitItem()
            assertTrue(successState is CardapioResult.Success)
            assertTrue(successState.value.isEmpty())
        }
    }

    @Test
    fun `estado inicial deve ser Loading`() {
        // Given/When
        val initialState = viewModel.uiState.value
        
        // Then
        assertTrue(initialState is CardapioResult.Loading, "Estado inicial deve ser Loading")
    }

    @Test
    fun `propriedades auxiliares do CardapioResult devem funcionar corretamente`() {
        // Given
        val loadingState = CardapioResult.Loading()
        val successState = CardapioResult.Success(listOf(MenuItem()))
        val errorState = CardapioResult.Failure(Exception("Erro"))
        
        // Then
        assertTrue(loadingState.isLoading && !loadingState.isSuccess && !loadingState.isFailure)
        assertTrue(!successState.isLoading && successState.isSuccess && !successState.isFailure)
        assertTrue(!errorState.isLoading && !errorState.isSuccess && errorState.isFailure)
    }
}

