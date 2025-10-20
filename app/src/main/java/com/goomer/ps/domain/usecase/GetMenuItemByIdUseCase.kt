package com.goomer.ps.domain.usecase

import com.goomer.ps.domain.model.MenuItem
import com.goomer.ps.domain.model.CardapioResult
import com.goomer.ps.domain.repository.CardapioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class GetMenuItemByIdUseCase(
    private val repository: CardapioRepository
) {

    /**
     * Executa o use case para obter um item específico do menu.
     *
     * @param itemId ID do item a ser buscado
     * @return Flow de MenuResult contendo o item ou erro
     */
    fun invoke(itemId: Int): Flow<CardapioResult<MenuItem>> {
        if (itemId <= 0) {
            return flowOf(
                CardapioResult.Error(
                    message = "ID inválido: $itemId",
                    throwable = IllegalArgumentException("ID deve ser maior que zero")
                )
            )
        }
        
        return flow {
            emit(CardapioResult.Loading)
            try {
                repository.getMenuItemById(itemId).collect { item ->
                    if (item != null) {
                        emit(CardapioResult.Success(item))
                    } else {
                        emit(
                            CardapioResult.Error(
                                message = "Item não encontrado: ID $itemId",
                                throwable = null
                            )
                        )
                    }
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


