package com.goomer.ps.data.repository

import com.goomer.ps.data.datasource.LocalCardapioDataSource
import com.goomer.ps.data.dto.MenuItemDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class CardapioRepositoryTest {
    private lateinit var localDataSource: LocalCardapioDataSource
    private lateinit var repository: CardapioRepositoryImpl

    @Before
    fun setup() {
        localDataSource = mock()
        repository = CardapioRepositoryImpl(localDataSource)
    }

    @Test
    fun `getMenuItems deve mapear DTOs para Domain corretamente`() =
        runTest {
            // Given
            val dtoList =
                listOf(
                    MenuItemDto(
                        id = 1,
                        name = "Pizza Margherita",
                        description = "Pizza com tomate e manjericão",
                        price = 29.90,
                        imageUrl = "https://example.com/pizza.jpg",
                    ),
                    MenuItemDto(
                        id = 2,
                        name = "Hamburguer Artesanal",
                        description = "Hamburguer com carne artesanal",
                        price = 19.90,
                        imageUrl = "https://example.com/burger.jpg",
                    ),
                )

            whenever(localDataSource.loadMenuItems()).thenReturn(dtoList)

            // When
            val result = repository.getMenuItems().toList().first()

            // Then
            assertEquals(2, result.size)

            val firstItem = result[0]
            assertEquals(1, firstItem.id)
            assertEquals("Pizza Margherita", firstItem.name)
            assertEquals("Pizza com tomate e manjericão", firstItem.description)
            assertEquals(29.90, firstItem.price, 0.01)
            assertEquals("https://example.com/pizza.jpg", firstItem.imageUrl)

            val secondItem = result[1]
            assertEquals(2, secondItem.id)
            assertEquals("Hamburguer Artesanal", secondItem.name)
            assertEquals("Hamburguer com carne artesanal", secondItem.description)
            assertEquals(19.90, secondItem.price, 0.01)
            assertEquals("https://example.com/burger.jpg", secondItem.imageUrl)
        }

    @Test
    fun `getMenuItems deve retornar lista vazia quando não há itens`() =
        runTest {
            // Given
            whenever(localDataSource.loadMenuItems()).thenReturn(emptyList())

            // When
            val result = repository.getMenuItems().toList().first()

            // Then
            assertTrue(result.isEmpty())
        }

    @Test
    fun `getMenuItems deve lançar exceção quando DataSource falha`() =
        runTest {
            // Given
            val exception = RuntimeException("Erro ao ler arquivo")
            whenever(localDataSource.loadMenuItems()).thenThrow(exception)

            // When/Then
            try {
                repository.getMenuItems().toList().first()
                fail("Deveria lançar exceção")
            } catch (e: Exception) {
                assertTrue(e.message?.contains("Erro no repositório") == true)
                assertTrue(e.message?.contains("Erro ao ler arquivo") == true)
                assertSame(exception, e.cause)
            }
        }

    @Test
    fun `getMenuItemById deve retornar item correto quando existe`() =
        runTest {
            // Given
            val dtoList =
                listOf(
                    MenuItemDto(id = 1, name = "Pizza", description = "Deliciosa pizza", price = 29.90, imageUrl = ""),
                    MenuItemDto(
                        id = 2,
                        name = "Hamburguer",
                        description = "Hamburguer artesanal",
                        price = 19.90,
                        imageUrl = "",
                    ),
                    MenuItemDto(id = 3, name = "Salada", description = "Salada fresca", price = 15.90, imageUrl = ""),
                )

            whenever(localDataSource.loadMenuItems()).thenReturn(dtoList)

            // When
            val result = repository.getMenuItemById(2).toList().first()

            // Then
            assertNotNull(result)
            assertEquals(2, result?.id)
            assertEquals("Hamburguer", result?.name)
            assertEquals("Hamburguer artesanal", result?.description)
            assertEquals(19.90, result?.price ?: 0.0, 0.01)
        }

    @Test
    fun `getMenuItemById deve retornar null quando item não existe`() =
        runTest {
            // Given
            val dtoList =
                listOf(
                    MenuItemDto(id = 1, name = "Pizza", description = "Deliciosa pizza", price = 29.90, imageUrl = ""),
                )

            whenever(localDataSource.loadMenuItems()).thenReturn(dtoList)

            // When
            val result = repository.getMenuItemById(999).toList().first()

            // Then
            assertNull(result)
        }

    @Test
    fun `getMenuItemById deve lançar exceção quando DataSource falha`() =
        runTest {
            // Given
            val exception = RuntimeException("Erro ao ler arquivo")
            whenever(localDataSource.loadMenuItems()).thenThrow(exception)

            // When/Then
            try {
                repository.getMenuItemById(1).toList().first()
                fail("Deveria lançar exceção")
            } catch (e: Exception) {
                assertTrue(e.message?.contains("Erro no repositório") == true)
                assertTrue(e.message?.contains("Erro ao ler arquivo") == true)
                assertSame(exception, e.cause)
            }
        }

    @Test
    fun `getMenuItemById deve retornar primeiro item quando há múltiplos com mesmo ID`() =
        runTest {
            // Given
            val dtoList =
                listOf(
                    MenuItemDto(id = 1, name = "Pizza 1", description = "Primeira pizza", price = 29.90, imageUrl = ""),
                    MenuItemDto(id = 1, name = "Pizza 2", description = "Segunda pizza", price = 35.90, imageUrl = ""),
                )

            whenever(localDataSource.loadMenuItems()).thenReturn(dtoList)

            // When
            val result = repository.getMenuItemById(1).toList().first()

            // Then
            assertNotNull(result)
            assertEquals("Pizza 1", result?.name)
            assertEquals(29.90, result?.price ?: 0.0, 0.01)
        }

    @Test
    fun `mapeamento deve preservar todos os campos do DTO`() =
        runTest {
            // Given
            val dtoWithAllFields =
                MenuItemDto(
                    id = 42,
                    name = "Teste Completo",
                    description = "Descrição detalhada do item",
                    price = 99.99,
                    imageUrl = "https://example.com/image.png",
                )

            whenever(localDataSource.loadMenuItems()).thenReturn(listOf(dtoWithAllFields))

            // When
            val result = repository.getMenuItems().toList().first()
            val domainItem = result.first()

            // Then
            assertEquals(dtoWithAllFields.id, domainItem.id)
            assertEquals(dtoWithAllFields.name, domainItem.name)
            assertEquals(dtoWithAllFields.description, domainItem.description)
            assertEquals(dtoWithAllFields.price, domainItem.price, 0.01)
            assertEquals(dtoWithAllFields.imageUrl, domainItem.imageUrl)
        }
}
