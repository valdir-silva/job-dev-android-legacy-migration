package com.goomer.ps.data.mapper

import com.goomer.ps.data.dto.MenuItemDto
import com.goomer.ps.data.mapper.MenuItemMapper.toDomain
import com.goomer.ps.data.mapper.MenuItemMapper.toDomainList
import com.goomer.ps.data.mapper.MenuItemMapper.toDto
import com.goomer.ps.data.mapper.MenuItemMapper.toDtoList
import com.goomer.ps.domain.model.MenuItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MenuItemMapperTest {
    @Test
    fun `toDomain deve mapear DTO para Domain corretamente`() {
        // Given
        val dto =
            MenuItemDto(
                id = 1,
                name = "Pizza Margherita",
                description = "Pizza com tomate e manjericão",
                price = 29.90,
                imageUrl = "https://example.com/pizza.jpg",
            )

        // When
        val domain = dto.toDomain()

        // Then
        assertEquals(dto.id, domain.id)
        assertEquals(dto.name, domain.name)
        assertEquals(dto.description, domain.description)
        assertEquals(dto.price, domain.price, 0.01)
        assertEquals(dto.imageUrl, domain.imageUrl)
    }

    @Test
    fun `toDomainList deve mapear lista de DTOs para Domain`() {
        // Given
        val dtoList =
            listOf(
                MenuItemDto(id = 1, name = "Pizza", description = "Pizza", price = 29.90, imageUrl = ""),
                MenuItemDto(id = 2, name = "Hamburguer", description = "Hamburguer", price = 19.90, imageUrl = ""),
            )

        // When
        val domainList = dtoList.toDomainList()

        // Then
        assertEquals(2, domainList.size)
        assertEquals(1, domainList[0].id)
        assertEquals("Pizza", domainList[0].name)
        assertEquals(2, domainList[1].id)
        assertEquals("Hamburguer", domainList[1].name)
    }

    @Test
    fun `toDomainList deve retornar lista vazia quando DTO é vazio`() {
        // Given
        val dtoList = emptyList<MenuItemDto>()

        // When
        val domainList = dtoList.toDomainList()

        // Then
        assertTrue(domainList.isEmpty())
    }

    @Test
    fun `toDto deve mapear Domain para DTO corretamente`() {
        // Given
        val domain =
            MenuItem(
                id = 1,
                name = "Pizza Margherita",
                description = "Pizza com tomate e manjericão",
                price = 29.90,
                imageUrl = "https://example.com/pizza.jpg",
            )

        // When
        val dto = domain.toDto()

        // Then
        assertEquals(domain.id, dto.id)
        assertEquals(domain.name, dto.name)
        assertEquals(domain.description, dto.description)
        assertEquals(domain.price, dto.price, 0.01)
        assertEquals(domain.imageUrl, dto.imageUrl)
    }

    @Test
    fun `toDto deve tratar valores null corretamente`() {
        // Given
        val domain =
            MenuItem(
                id = 1,
                name = null,
                description = null,
                price = 29.90,
                imageUrl = null,
            )

        // When
        val dto = domain.toDto()

        // Then
        assertEquals(1, dto.id)
        assertEquals("", dto.name)
        assertEquals("", dto.description)
        assertEquals(29.90, dto.price, 0.01)
        assertEquals("", dto.imageUrl)
    }

    @Test
    fun `toDtoList deve mapear lista de Domain para DTO`() {
        // Given
        val domainList =
            listOf(
                MenuItem(id = 1, name = "Pizza", description = "Pizza", price = 29.90, imageUrl = ""),
                MenuItem(id = 2, name = "Hamburguer", description = "Hamburguer", price = 19.90, imageUrl = ""),
            )

        // When
        val dtoList = domainList.toDtoList()

        // Then
        assertEquals(2, dtoList.size)
        assertEquals(1, dtoList[0].id)
        assertEquals("Pizza", dtoList[0].name)
        assertEquals(2, dtoList[1].id)
        assertEquals("Hamburguer", dtoList[1].name)
    }

    @Test
    fun `toDtoList deve retornar lista vazia quando Domain é vazio`() {
        // Given
        val domainList = emptyList<MenuItem>()

        // When
        val dtoList = domainList.toDtoList()

        // Then
        assertTrue(dtoList.isEmpty())
    }

    @Test
    fun `mapeamento bidirecional deve preservar dados`() {
        // Given
        val originalDto =
            MenuItemDto(
                id = 1,
                name = "Pizza Margherita",
                description = "Pizza com tomate e manjericão",
                price = 29.90,
                imageUrl = "https://example.com/pizza.jpg",
            )

        // When
        val domain = originalDto.toDomain()
        val backToDto = domain.toDto()

        // Then
        assertEquals(originalDto.id, backToDto.id)
        assertEquals(originalDto.name, backToDto.name)
        assertEquals(originalDto.description, backToDto.description)
        assertEquals(originalDto.price, backToDto.price, 0.01)
        assertEquals(originalDto.imageUrl, backToDto.imageUrl)
    }

    @Test
    fun `mapeamento deve funcionar com preços decimais`() {
        // Given
        val dto =
            MenuItemDto(
                id = 1,
                name = "Item com preço decimal",
                description = "Teste de precisão",
                price = 19.999,
                imageUrl = "",
            )

        // When
        val domain = dto.toDomain()

        // Then
        assertEquals(19.999, domain.price, 0.0001)
    }

    @Test
    fun `mapeamento deve funcionar com URLs longas`() {
        // Given
        val longUrl = "https://example.com/images/products/pizza-margherita-large-resolution-high-quality.jpg"
        val dto =
            MenuItemDto(
                id = 1,
                name = "Pizza",
                description = "Pizza",
                price = 29.90,
                imageUrl = longUrl,
            )

        // When
        val domain = dto.toDomain()

        // Then
        assertEquals(longUrl, domain.imageUrl)
    }
}
