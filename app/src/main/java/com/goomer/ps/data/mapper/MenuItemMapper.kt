package com.goomer.ps.data.mapper

import com.goomer.ps.data.dto.MenuItemDto
import com.goomer.ps.domain.model.MenuItem

object MenuItemMapper {
    fun MenuItemDto.toDomain() = MenuItem(
        id = id,
        name = name,
        description = description,
        price = price,
        imageUrl =imageUrl,
    )

    fun List<MenuItemDto>.toDomainList() = map { it.toDomain() }

    fun MenuItem.toDto() = MenuItemDto(
        id = id,
        name = name.orEmpty(),    description = description.orEmpty(),
        price = price,
        imageUrl = imageUrl.orEmpty()
    )

    fun List<MenuItem>.toDtoList() = map { it.toDto() }
}
