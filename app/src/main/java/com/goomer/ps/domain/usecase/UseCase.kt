package com.goomer.ps.domain.usecase

import com.goomer.ps.domain.model.CardapioResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Classe base abstrata para UseCases que retornam um único resultado.
 *
 * Uso:
 * ```kotlin
 * class GetMenuItemByIdUseCase(private val repository: MenuRepository) :
 *     SampleUseCase<MenuItem?, Int>() {
 *
 *     override suspend fun execute(parameters: Int) = repository.getMenuItemById(parameters)
 * }
 *
 * // Chamada
 * val result = useCase(itemId)
 * when (result) {
 *     is CardapioResult.Success -> { /* tratar sucesso */ }
 *     is CardapioResult.Failure -> { /* tratar erro */ }
 * }
 * ```
 *
 * @param T Tipo de dados retornado pelo UseCase
 * @param P Tipo de parâmetros de entrada (use Unit se não houver parâmetros)
 */
abstract class UseCase<out T, in P> {
    /**
     * @param parameters Parâmetros de entrada para o UseCase
     * @return Resultado da operação (CardapioResult)
     */
    protected abstract suspend fun execute(parameters: P): CardapioResult<T>

    /**
     * @param parameters Parâmetros de entrada para o UseCase
     * @return Resultado da operação (CardapioResult)
     */
    suspend operator fun invoke(parameters: P): CardapioResult<T> =
        withContext(Dispatchers.IO) {
            try {
                execute(parameters)
            } catch (e: Throwable) {
                CardapioResult.Failure(
                    throwable = e,
                )
            }
        }
}

/**
 * Classe base abstrata para UseCases que retornam um resultado sem parâmetros.
 *
 * Uso:
 * ```kotlin
 * class GetMenuItemsUseCase(private val repository: MenuRepository) :
 *     SampleUseCase<List<MenuItem>, Unit>() {
 *
 *     override suspend fun execute(parameters: Unit) = repository.getMenuItems()
 * }
 *
 * // Chamada
 * val result = useCase()
 * ```
 *
 * @param T Tipo de dados retornado pelo UseCase
 */
abstract class UseCaseNoParams<out T> : UseCase<T, Unit>() {
    /**
     * @return Resultado da operação (CardapioResult)
     */
    suspend operator fun invoke(): CardapioResult<T> = invoke(Unit)
}
