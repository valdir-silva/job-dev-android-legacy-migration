package com.goomer.ps.domain.repository

import com.goomer.ps.domain.model.MenuItem
import kotlinx.coroutines.flow.Flow

interface CardapioRepository {
    /**
     * @return Flow que emite uma lista de MenuItem
     * @throws Exception se houver erro ao carregar os dados
     */
    suspend fun getMenuItems(): Flow<List<MenuItem>>

    /**
     * @param itemId ID do item a ser buscado
     * @return Flow que emite o MenuItem encontrado ou null se não existir
     * @throws Exception se houver erro ao carregar os dados
     */
    suspend fun getMenuItemById(itemId: Int): Flow<MenuItem?>
}
