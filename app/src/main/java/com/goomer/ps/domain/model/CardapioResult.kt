package com.goomer.ps.domain.model

/**
 * @param R Tipo do dado retornado em caso de sucesso
 */
sealed class CardapioResult<out R>(val content: R? = null) {

    /**
     * @param value Dados retornados pela operação
     */
    data class Success<out R>(val value: R) : CardapioResult<R>(value)
    
    /**
     * @param throwable Exceção que causou a falha (opcional)
     */
    data class Failure(val throwable: Throwable? = null) : CardapioResult<Nothing>()
    data class Loading(val unit: Unit = Unit) : CardapioResult<Nothing>()

    val isSuccess: Boolean
        get() = this is Success
    val isFailure: Boolean
        get() = this is Failure
    val isLoading: Boolean
        get() = this is Loading

    /**
     * @return Valor em caso de Success, null para Loading ou Failure
     */
    fun getOrNull(): R? = when (this) {
        is Success -> content
        else -> null
    }

    /**
     * @return Throwable em caso de Failure, null para Loading ou Success
     */
    fun throwableOrNull(): Throwable? = when (this) {
        is Failure -> throwable
        else -> null
    }
}

/**
 * @param block Bloco de código a ser executado
 * @return CardapioResult com o resultado da execução
 */
inline fun <T> onResult(block: () -> T): CardapioResult<T> {
    return try {
        CardapioResult.Success(requireNotNull(block()))
    } catch (e: Throwable) {
        CardapioResult.Failure(e)
    }
}

/**
 * @param block Bloco de código a ser executado com o valor de sucesso
 * @return CardapioResult original ou Failure se o bloco lançar exceção
 */
@Suppress("UNCHECKED_CAST")
inline fun <T> CardapioResult<T>.onResultSuccess(block: (T) -> Unit): CardapioResult<T> {
    return takeIf { isSuccess }?.let {
        onResult {
            block(requireNotNull(content as T))
        }.throwableOrNull()
    }?.let { throwable ->
        CardapioResult.Failure(throwable)
    } ?: this
}

/**
 * @param block Bloco de código a ser executado com a exceção
 * @return CardapioResult original ou Failure se o bloco lançar exceção
 */
inline fun <T> CardapioResult<T>.onResultFailure(block: (Throwable) -> Unit): CardapioResult<T> {
    return throwableOrNull()?.let { throwable ->
        onResult {
            block(throwable)
        }.throwableOrNull()
    }?.let { throwable ->
        CardapioResult.Failure(throwable)
    } ?: this
}