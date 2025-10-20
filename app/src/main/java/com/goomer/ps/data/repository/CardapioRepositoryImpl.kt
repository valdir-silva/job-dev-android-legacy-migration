package com.goomer.ps.data.repository

import com.goomer.ps.data.datasource.LocalCardapioDataSource
import com.goomer.ps.data.mapper.MenuItemMapper.toDomainList
import com.goomer.ps.domain.model.MenuItem
import com.goomer.ps.domain.repository.CardapioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Implementação concreta do MenuRepository.
 * 
 * Esta classe é responsável por:
 * - Coordenar fontes de dados (local, remoto, cache)
 * - Transformar DTOs em modelos de domínio
 * - Tratar erros de acesso aos dados
 * 
 * Atualmente implementa apenas LocalMenuDataSource (fase de migração).
 * Futuramente, esta classe irá orquestrar múltiplas fontes de dados.
 */
class CardapioRepositoryImpl(
    private val localDataSource: LocalCardapioDataSource
) : CardapioRepository {
    
    override suspend fun getMenuItems(): Flow<List<MenuItem>> = flow {
        try {
            val dtoList = localDataSource.loadMenuItems()
            val domainList = dtoList.toDomainList()
            emit(domainList)
        } catch (e: Exception) {
            throw Exception("Erro no repositório ao obter itens do menu: ${e.message}", e)
        }
    }

    override suspend fun getMenuItemById(itemId: Int): Flow<MenuItem?> = flow {
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
