package com.goomer.ps.domain.usecase

import com.goomer.ps.domain.model.CardapioResult
import com.goomer.ps.domain.model.MenuItem
import com.goomer.ps.domain.repository.CardapioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class GetMenuItemByIdUseCase(
    private val repository: CardapioRepository,
) : SampleFlowUseCase<MenuItem, Int>() {
    /**
     * @param parameters ID do item a ser buscado
     * @return Flow com CardapioResult contendo o item
     */
    override suspend fun execute(parameters: Int): Flow<CardapioResult<MenuItem>> {
        if (parameters <= 0) {
            return flowOf(
                CardapioResult.Failure(
                    throwable = IllegalArgumentException("ID inválido: $parameters. ID deve ser maior que zero"),
                ),
            )
        }

        return repository.getMenuItemById(parameters).map { item ->
            if (item != null) {
                CardapioResult.Success(item)
            } else {
                CardapioResult.Failure(
                    throwable = Exception("Item não encontrado: ID $parameters"),
                )
            }
        }
    }
}
