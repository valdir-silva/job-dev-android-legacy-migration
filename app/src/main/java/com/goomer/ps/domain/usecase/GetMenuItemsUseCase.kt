package com.goomer.ps.domain.usecase

import com.goomer.ps.domain.model.MenuItem
import com.goomer.ps.domain.model.CardapioResult
import com.goomer.ps.domain.repository.CardapioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMenuItemsUseCase(
    private val repository: CardapioRepository
) {

    /**
     * Executa o use case para obter todos os itens do menu.
     *
     * @return Flow de MenuResult contendo a lista de itens ou erro
     */
    fun invoke(): Flow<CardapioResult<List<MenuItem>>> {
        return flow {
            emit(CardapioResult.Loading)
            try {
                repository.getMenuItems().collect { items ->
                    emit(CardapioResult.Success(items))
                }
            } catch (exception: Exception) {
                emit(
                    CardapioResult.Error(
                        message = "Erro ao carregar itens do menu: ${exception.message}",
                        throwable = exception
                    )
                )
            }
        }
    }

    /**
     * Executa o use case para obter um item específico do menu.
     *
     * @param itemId ID do item a ser buscado
     * @return Flow de MenuResult contendo o item ou erro
     */
    fun invoke(itemId: Int): Flow<CardapioResult<MenuItem?>> {
        return flow {
            emit(CardapioResult.Loading)
            try {
                repository.getMenuItemById(itemId).collect { item ->
                    emit(CardapioResult.Success(item))
                }
            } catch (exception: Exception) {
                emit(
                    CardapioResult.Error(
                        message = "Erro ao carregar item do menu: ${exception.message}",
                        throwable = exception
                    )
                )
            }
        }
    }
}


