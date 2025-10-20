package com.goomer.ps.domain.repository

import com.goomer.ps.domain.model.MenuItem
import kotlinx.coroutines.flow.Flow

/**
 * Interface do repositório de Menu na camada de domínio.
 * Define o contrato para acesso aos dados de menu, abstraindo a origem dos dados
 * (local, remoto, cache, etc.).
 */
interface CardapioRepository {
    /**
     * Obtém todos os itens do menu.
     *
     * @return Flow que emite uma lista de MenuItem
     * @throws Exception se houver erro ao carregar os dados
     */
    suspend fun getMenuItems(): Flow<List<MenuItem>>

    /**
     * Obtém um item específico do menu por ID.
     *
     * @param itemId ID do item a ser buscado
     * @return Flow que emite o MenuItem encontrado ou null se não existir
     * @throws Exception se houver erro ao carregar os dados
     */
    suspend fun getMenuItemById(itemId: Int): Flow<MenuItem?>
}
