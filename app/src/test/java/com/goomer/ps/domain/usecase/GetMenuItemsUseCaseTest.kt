package com.goomer.ps.domain.usecase

import com.goomer.ps.domain.model.CardapioResult
import com.goomer.ps.domain.model.MenuItem
import com.goomer.ps.domain.repository.CardapioRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
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
    fun `invoke should return Success when repository returns data`() =
        runTest {
            // Given
            val expectedItems =
                listOf(
                    MenuItem(id = 1, name = "Pizza", price = 25.0),
                    MenuItem(id = 2, name = "Hamburger", price = 15.0),
                )
            whenever(repository.getMenuItems()).thenReturn(flowOf(expectedItems))

            // When
            val results = useCase.invoke().toList()
            val result = results[1]

            // Then
            assertTrue(result is CardapioResult.Success)
            assertEquals(expectedItems, (result as CardapioResult.Success).value)
        }

    @Test
    fun `invoke should return Error when repository throws exception`() =
        runTest {
            // Given
            val exception = RuntimeException("Network error")
            whenever(repository.getMenuItems()).thenThrow(exception)

            // When
            val results = useCase.invoke().toList()
            val result = results[1]

            // Then
            assertTrue(result is CardapioResult.Failure)
            val errorResult = result as CardapioResult.Failure
            assertEquals("Network error", errorResult.throwable?.message)
            assertNotNull(errorResult.throwable)
        }

    @Test
    fun `invoke should emit Loading first`() =
        runTest {
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
}
