package com.goomer.ps.data.repository

import com.goomer.ps.data.datasource.LocalMenuDataSource
import com.goomer.ps.data.mapper.MenuItemMapper.toDomainList
import com.goomer.ps.domain.model.MenuItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MenuRepositoryImpl(
    private val localDataSource: LocalMenuDataSource
) {
    fun getMenuItems(): Flow<List<MenuItem>> = flow {
        try {
            val dtoList = localDataSource.loadMenuItems()
            val domainList = dtoList.toDomainList()
            emit(domainList)
        } catch (e: Exception) {
            throw Exception("Erro no repositório ao obter itens do menu: ${e.message}", e)
        }
    }

    fun getMenuItemById(itemId: Int): Flow<MenuItem?> = flow {
        try {
            val dtoList = localDataSource.loadMenuItems()
            val domainList = dtoList.toDomainList()
            val item = domainList.find { it.id == itemId }
            emit(item)
        } catch (e: Exception) {
            throw Exception("Erro no repositório ao obter item do menu: ${e.message}", e)
        }
    }
}
