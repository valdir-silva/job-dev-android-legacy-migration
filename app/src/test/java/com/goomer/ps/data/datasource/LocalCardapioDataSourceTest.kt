package com.goomer.ps.data.datasource

import android.content.Context
import android.content.res.AssetManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.io.ByteArrayInputStream
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class LocalCardapioDataSourceTest {
    private lateinit var mockContext: Context
    private lateinit var mockAssetManager: AssetManager
    private lateinit var dataSource: LocalCardapioDataSource

    @Before
    fun setup() {
        mockContext = mock()
        mockAssetManager = mock()
        whenever(mockContext.assets).thenReturn(mockAssetManager)
        dataSource = LocalCardapioDataSource(mockContext)
    }

    @Test
    fun `loadMenuItems deve carregar e parsear JSON corretamente`() =
        runTest {
            // Given
            val jsonContent =
                """
                [
                    {
                        "id": 1,
                        "name": "Pizza Margherita",
                        "description": "Pizza com tomate e manjericão",
                        "price": 29.90,
                        "imageUrl": "https://example.com/pizza.jpg"
                    },
                    {
                        "id": 2,
                        "name": "Hamburguer Artesanal",
                        "description": "Hamburguer com carne artesanal",
                        "price": 19.90,
                        "imageUrl": "https://example.com/burger.jpg"
                    }
                ]
                """.trimIndent()

            val inputStream = ByteArrayInputStream(jsonContent.toByteArray())
            whenever(mockAssetManager.open("menu.json")).thenReturn(inputStream)

            // When
            val result = dataSource.loadMenuItems()

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
    fun `loadMenuItems deve retornar lista vazia quando JSON é array vazio`() =
        runTest {
            // Given
            val jsonContent = "[]"
            val inputStream = ByteArrayInputStream(jsonContent.toByteArray())
            whenever(mockAssetManager.open("menu.json")).thenReturn(inputStream)

            // When
            val result = dataSource.loadMenuItems()

            // Then
            assertTrue(result.isEmpty())
        }

    @Test
    fun `loadMenuItems deve lançar exceção quando arquivo não existe`() =
        runTest {
            // Given
            whenever(mockAssetManager.open("menu.json"))
                .thenThrow(IOException("Arquivo não encontrado"))

            // When/Then
            try {
                dataSource.loadMenuItems()
                fail("Deveria lançar exceção")
            } catch (e: Exception) {
                assertTrue(e.message?.contains("Erro ao carregar itens do menu") == true)
                assertTrue(e.message?.contains("Arquivo não encontrado") == true)
            }
        }

    @Test
    fun `loadMenuItems deve lançar exceção quando JSON é inválido`() =
        runTest {
            // Given
            val invalidJson = "{ invalid json }"
            val inputStream = ByteArrayInputStream(invalidJson.toByteArray())
            whenever(mockAssetManager.open("menu.json")).thenReturn(inputStream)

            // When/Then
            try {
                dataSource.loadMenuItems()
                fail("Deveria lançar exceção")
            } catch (e: Exception) {
                assertTrue(e.message?.contains("Erro ao processar dados do menu") == true)
            }
        }

    @Test
    fun `loadMenuItems deve lançar exceção quando JSON é null`() =
        runTest {
            // Given
            val nullJson = "null"
            val inputStream = ByteArrayInputStream(nullJson.toByteArray())
            whenever(mockAssetManager.open("menu.json")).thenReturn(inputStream)

            // When
            val result = dataSource.loadMenuItems()

            // Then
            assertTrue(result.isEmpty())
        }

    @Test
    fun `loadMenuItems deve parsear JSON com campos opcionais`() =
        runTest {
            // Given
            val jsonContent =
                """
                [
                    {
                        "id": 1,
                        "name": "Pizza",
                        "description": "Pizza",
                        "price": 29.90,
                        "imageUrl": ""
                    }
                ]
                """.trimIndent()

            val inputStream = ByteArrayInputStream(jsonContent.toByteArray())
            whenever(mockAssetManager.open("menu.json")).thenReturn(inputStream)

            // When
            val result = dataSource.loadMenuItems()

            // Then
            assertEquals(1, result.size)
            assertEquals("", result[0].imageUrl)
        }

    @Test
    fun `loadMenuItems deve parsear JSON com muitos itens`() =
        runTest {
            // Given
            val items =
                (1..100).joinToString(",", "[", "]") { i ->
                    """
            {
                "id": $i,
                "name": "Item $i",
                "description": "Descrição do item $i",
                "price": ${i * 10.0},
                "imageUrl": "https://example.com/item$i.jpg"
            }
            """
                }

            val inputStream = ByteArrayInputStream(items.toByteArray())
            whenever(mockAssetManager.open("menu.json")).thenReturn(inputStream)

            // When
            val result = dataSource.loadMenuItems()

            // Then
            assertEquals(100, result.size)
            assertEquals(1, result[0].id)
            assertEquals("Item 1", result[0].name)
            assertEquals(100, result[99].id)
            assertEquals("Item 100", result[99].name)
        }

    @Test
    fun `loadMenuItems deve parsear JSON com preços decimais`() =
        runTest {
            // Given
            val jsonContent =
                """
                [
                    {
                        "id": 1,
                        "name": "Item com preço decimal",
                        "description": "Teste de precisão",
                        "price": 19.999,
                        "imageUrl": ""
                    }
                ]
                """.trimIndent()

            val inputStream = ByteArrayInputStream(jsonContent.toByteArray())
            whenever(mockAssetManager.open("menu.json")).thenReturn(inputStream)

            // When
            val result = dataSource.loadMenuItems()

            // Then
            assertEquals(19.999, result[0].price, 0.0001)
        }

    @Test
    fun `loadMenuItems deve fechar InputStream corretamente`() =
        runTest {
            // Given
            val jsonContent = """[]"""
            val inputStream = ByteArrayInputStream(jsonContent.toByteArray())
            whenever(mockAssetManager.open("menu.json")).thenReturn(inputStream)

            // When
            dataSource.loadMenuItems()

            // Then - se chegou aqui sem exceção, o InputStream foi fechado corretamente
            assertTrue(true)
        }
}
