package com.goomer.ps.data

import com.goomer.ps.data.dto.MenuItemDto
import com.goomer.ps.data.mapper.MenuItemMapper.toDomain
import com.goomer.ps.data.mapper.MenuItemMapper.toDomainList
import com.goomer.ps.data.mapper.MenuItemMapper.toDto
import com.goomer.ps.domain.model.MenuItem
import org.junit.Assert.assertEquals
import org.junit.Test

class MenuItemMapperTest {

    @Test
    fun `toDomain should convert DTO to domain model correctly`() {
        // Given
        val dto = MenuItemDto(
            id = 1,
            name = "Hambúrguer Clássico",
            description = "Pão, carne 150g, queijo, alface e tomate.",
            price = 24.90,
            imageUrl = "https://example.com/image.jpg"
        )

        // When
        val domain = dto.toDomain()

        // Then
        assertEquals("ID deve ser igual", 1, domain.id)
        assertEquals("Nome deve ser igual", "Hambúrguer Clássico", domain.name)
        assertEquals(
            "Descrição deve ser igual",
            "Pão, carne 150g, queijo, alface e tomate.",
            domain.description
        )
        assertEquals("Preço deve ser igual", 24.90, domain.price, 0.001)
        assertEquals(
            "URL da imagem deve ser igual",
            "https://example.com/image.jpg",
            domain.imageUrl
        )
    }

    @Test
    fun `toDomainList should convert list of DTOs to domain models correctly`() {
        // Given
        val dtoList = listOf(
            MenuItemDto(1, "Item 1", "Desc 1", 10.0, "url1"),
            MenuItemDto(2, "Item 2", "Desc 2", 20.0, "url2"),
        )

        // When
        val domainList = dtoList.toDomainList()

        // Then
        assertEquals("Lista deve ter 2 itens", 2, domainList.size)
        assertEquals("Primeiro item ID deve ser 1", 1, domainList[0].id)
        assertEquals("Segundo item ID deve ser 2", 2, domainList[1].id)
    }

    @Test
    fun `toDto should convert domain model to DTO correctly`() {
        // Given
        val domain = MenuItem(
            id = 1,
            name = "Hambúrguer Clássico",
            description = "Pão, carne 150g, queijo, alface e tomate.",
            price = 24.90,
            imageUrl = "https://example.com/image.jpg",
        )

        // When
        val dto = domain.toDto()

        // Then
        assertEquals("ID deve ser igual", 1, dto.id)
        assertEquals("Nome deve ser igual", "Hambúrguer Clássico", dto.name)
        assertEquals(
            "Descrição deve ser igual",
            "Pão, carne 150g, queijo, alface e tomate.",
            dto.description
        )
        assertEquals("Preço deve ser igual", 24.90, dto.price, 0.001)
        assertEquals("URL da imagem deve ser igual", "https://example.com/image.jpg", dto.imageUrl)
    }

    @Test
    fun `toDto should handle null values correctly`() {
        // Given
        val domain = MenuItem(
            id = 1,
            name = null,
            description = null,
            price = 0.0,
            imageUrl = null,
        )

        // When
        val dto = domain.toDto()

        // Then
        assertEquals("Nome nulo deve virar string vazia", "", dto.name)
        assertEquals("Descrição nula deve virar string vazia", "", dto.description)
        assertEquals("URL nula deve virar string vazia", "", dto.imageUrl)
    }
}
