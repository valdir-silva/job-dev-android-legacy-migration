package com.goomer.ps.domain.usecase

import com.goomer.ps.domain.model.MenuItem
import com.goomer.ps.domain.model.CardapioResult
import com.goomer.ps.domain.repository.CardapioRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class GetMenuItemsUseCaseTest {

    private lateinit var repository: CardapioRepository
    private lateinit var useCase: GetMenuItemsUseCase

    @Before
    fun setup() {
        repository = mock()
        useCase = GetMenuItemsUseCase(repository)
    }

    @Test
    fun `invoke should return Success when repository returns data`() = runTest {
        // Given
        val expectedItems = listOf(
            MenuItem(id = 1, name = "Pizza", price = 25.0),
            MenuItem(id = 2, name = "Hamburger", price = 15.0)
        )
        whenever(repository.getMenuItems()).thenReturn(flowOf(expectedItems))

        // When
        val results = useCase.invoke().toList()
        val result = results[1]

        // Then
        assertTrue(result is CardapioResult.Success)
        assertEquals(expectedItems, (result as CardapioResult.Success).data)
    }

    @Test
    fun `invoke should return Error when repository throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        whenever(repository.getMenuItems()).thenThrow(exception)

        // When
        val results = useCase.invoke().toList()
        val result = results[1]

        // Then
        assertTrue(result is CardapioResult.Error)
        val errorResult = result as CardapioResult.Error
        assertTrue(errorResult.message.contains("Erro ao carregar itens do menu"))
        assertNotNull(errorResult.throwable)
    }

    @Test
    fun `invoke should emit Loading first`() = runTest {
        // Given
        val expectedItems = listOf(MenuItem(id = 1, name = "Pizza", price = 25.0))
        whenever(repository.getMenuItems()).thenReturn(flowOf(expectedItems))

        // When
        val results = useCase.invoke().toList()

        // Then
        assertTrue("O primeiro valor deve ser Loading", results[0] is CardapioResult.Loading)
        assertTrue("O segundo valor deve ser Success", results[1] is CardapioResult.Success)
        assertEquals("Deve ter exatamente 2 valores", 2, results.size)
    }

    @Test
    fun `invoke with itemId should return Success when item exists`() = runTest {
        // Given
        val expectedItem = MenuItem(id = 1, name = "Pizza", price = 25.0)
        whenever(repository.getMenuItemById(1)).thenReturn(flowOf(expectedItem))

        // When
        val results = useCase.invoke(1).toList()
        val result = results[1]

        // Then
        assertTrue(result is CardapioResult.Success)
        assertEquals(expectedItem, (result as CardapioResult.Success).data)
    }

    @Test
    fun `invoke with itemId should return Success with null when item not found`() = runTest {
        // Given
        whenever(repository.getMenuItemById(999)).thenReturn(flowOf(null))

        // When
        val results = useCase.invoke(999).toList()
        val result = results[1]

        // Then
        assertTrue(result is CardapioResult.Success)
        assertNull((result as CardapioResult.Success).data)
    }
}

