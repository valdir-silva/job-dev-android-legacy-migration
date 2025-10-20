package com.goomer.ps.domain.usecase

import com.goomer.ps.domain.model.CardapioResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

/**
 * @param T Tipo de dados retornado pelo UseCase
 * @param P Tipo de parâmetros de entrada (use Unit se não houver parâmetros)
 */
abstract class SampleFlowUseCase<out T, in P> {
    /**
     * @param parameters Parâmetros de entrada para o UseCase
     * @return Flow com o resultado da operação (CardapioResult)
     */
    protected abstract suspend fun execute(parameters: P): Flow<CardapioResult<T>>

    /**
     * @param parameters Parâmetros de entrada para o UseCase
     * @return Flow com o resultado da operação (CardapioResult)
     */
    suspend operator fun invoke(parameters: P): Flow<CardapioResult<T>> =
        withContext(Dispatchers.IO) {
            try {
                execute(parameters)
                    .onStart { emit(CardapioResult.Loading()) }
                    .catch { e ->
                        emit(
                            CardapioResult.Failure(
                                throwable = e,
                            ),
                        )
                    }
            } catch (e: Throwable) {
                flowOf(
                    CardapioResult.Loading(),
                    CardapioResult.Failure(
                        throwable = e,
                    ),
                )
            }
        }
}

/**
 * @param T Tipo de dados retornado pelo UseCase
 */
abstract class SampleFlowUseCaseNoParams<out T> : SampleFlowUseCase<T, Unit>() {
    /**
     * @return Flow com o resultado da operação (CardapioResult)
     */
    suspend operator fun invoke(): Flow<CardapioResult<T>> = invoke(Unit)
}
