package com.goomer.ps.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.goomer.ps.data.dto.MenuItemDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.lang.reflect.Type

class GsonSerializationTest {
    private val gson = Gson()
    private val listType: Type = object : TypeToken<List<MenuItemDto>>() {}.type

    @Test
    fun `deserialize JSON should work correctly`() {
        // Given
        val json =
            """
            [
              {"id":1,"name":"Hambúrguer Clássico","description":"Pão, carne 150g, queijo, alface e tomate.","price":24.90,"imageUrl":""},
              {"id":2,"name":"Hambúrguer Bacon","description":"Carne 180g, bacon crocante, cheddar, maionese da casa.","price":29.90,"imageUrl":""}
            ]
            """.trimIndent()

        // When
        val items = gson.fromJson<List<MenuItemDto>>(json, listType)

        // Then
        assertNotNull("Lista de itens não deve ser nula", items)
        assertEquals("Deve ter exatamente 2 itens", 2, items.size)

        val firstItem = items[0]
        assertEquals("Primeiro item ID deve ser 1", 1, firstItem.id)
        assertEquals("Primeiro item nome deve ser correto", "Hambúrguer Clássico", firstItem.name)
        assertEquals("Primeiro item preço deve ser 24.90", 24.90, firstItem.price, 0.001)

        val secondItem = items[1]
        assertEquals("Segundo item ID deve ser 2", 2, secondItem.id)
        assertEquals("Segundo item nome deve ser correto", "Hambúrguer Bacon", secondItem.name)
        assertEquals("Segundo item preço deve ser 29.90", 29.90, secondItem.price, 0.001)
    }

    @Test
    fun `serialize to JSON should work correctly`() {
        // Given
        val items =
            listOf(
                MenuItemDto(1, "Hambúrguer Clássico", "Pão, carne 150g, queijo, alface e tomate.", 24.90, ""),
                MenuItemDto(2, "Hambúrguer Bacon", "Carne 180g, bacon crocante, cheddar, maionese da casa.", 29.90, ""),
            )

        // When
        val json = gson.toJson(items)

        // Then
        assertNotNull("JSON não deve ser nulo", json)
        assertTrue("JSON deve conter o nome do primeiro item", json.contains("Hambúrguer Clássico"))
        assertTrue("JSON deve conter o nome do segundo item", json.contains("Hambúrguer Bacon"))
        assertTrue("JSON deve conter o preço 24.90", json.contains("24.9"))
        assertTrue("JSON deve conter o preço 29.90", json.contains("29.9"))
    }

    @Test
    fun `deserialize single item should work correctly`() {
        // Given
        val json =
            """{"id":1,"name":"Hambúrguer Clássico","description":"Pão, carne 150g, queijo, alface e tomate.",""" +
                """"price":24.90,"imageUrl":""}"""

        // When
        val item = gson.fromJson(json, MenuItemDto::class.java)

        // Then
        assertNotNull("Item não deve ser nulo", item)
        assertEquals("ID deve ser 1", 1, item.id)
        assertEquals("Nome deve ser correto", "Hambúrguer Clássico", item.name)
        assertEquals("Descrição deve ser correta", "Pão, carne 150g, queijo, alface e tomate.", item.description)
        assertEquals("Preço deve ser 24.90", 24.90, item.price, 0.001)
        assertEquals("URL da imagem deve ser vazia", "", item.imageUrl)
    }
}
