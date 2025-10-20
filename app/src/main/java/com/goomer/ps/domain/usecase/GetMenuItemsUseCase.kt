package com.goomer.ps.domain.usecase

import com.goomer.ps.domain.model.CardapioResult
import com.goomer.ps.domain.model.MenuItem
import com.goomer.ps.domain.repository.CardapioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMenuItemsUseCase(
    private val repository: CardapioRepository,
) : SampleFlowUseCaseNoParams<List<MenuItem>>() {
    /**
     * @param parameters Unit
     * @return Flow com CardapioResult contendo a lista de itens
     */
    override suspend fun execute(parameters: Unit): Flow<CardapioResult<List<MenuItem>>> =
        repository.getMenuItems().map { items ->
            CardapioResult.Success(items)
        }
}
