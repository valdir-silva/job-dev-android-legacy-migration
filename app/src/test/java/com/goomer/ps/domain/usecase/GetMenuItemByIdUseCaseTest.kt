package com.goomer.ps.domain.usecase

import com.goomer.ps.domain.model.MenuItem
import com.goomer.ps.domain.model.CardapioResult
import com.goomer.ps.domain.repository.CardapioRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class GetMenuItemByIdUseCaseTest {

    private lateinit var repository: CardapioRepository
    private lateinit var useCase: GetMenuItemByIdUseCase

    @Before
    fun setup() {
        repository = mock()
        useCase = GetMenuItemByIdUseCase(repository)
    }

    @Test
    fun `invoke should return Success when item exists`() = runTest {
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
    fun `invoke should return Error when item not found`() = runTest {
        // Given
        whenever(repository.getMenuItemById(999)).thenReturn(flowOf(null))

        // When
        val results = useCase.invoke(999).toList()
        val result = results[1]

        // Then
        assertTrue(result is CardapioResult.Error)
        val errorResult = result as CardapioResult.Error
        assertTrue(errorResult.message.contains("Item não encontrado"))
    }

    @Test
    fun `invoke should return Error when id is invalid negative`() = runTest {
        // When
        val result = useCase.invoke(-1).first()

        // Then
        assertTrue(result is CardapioResult.Error)
        val errorResult = result as CardapioResult.Error
        assertTrue(errorResult.message.contains("ID inválido"))
    }

    @Test
    fun `invoke should return Error when id is zero`() = runTest {
        // When
        val result = useCase.invoke(0).first()

        // Then
        assertTrue(result is CardapioResult.Error)
        val errorResult = result as CardapioResult.Error
        assertTrue(errorResult.message.contains("ID inválido"))
    }

    @Test
    fun `invoke should return Error when repository throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Database error")
        whenever(repository.getMenuItemById(1)).thenThrow(exception)

        // When
        val results = useCase.invoke(1).toList()
        val result = results[1]

        // Then
        assertTrue(result is CardapioResult.Error)
        val errorResult = result as CardapioResult.Error
        assertTrue(errorResult.message.contains("Erro ao carregar item do menu"))
        assertNotNull(errorResult.throwable)
    }
}

