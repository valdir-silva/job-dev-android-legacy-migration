package com.goomer.ps.domain.model

/**
 * Sealed class que representa o resultado de operações na camada de domínio.
 * 
 * Esta classe encapsula os possíveis estados de uma operação assíncrona,
 * permitindo tratamento explícito de sucesso, erro e loading.
 *
 * Uso:
 * ```kotlin
 * when (result) {
 *     is MenuResult.Success -> { /* tratar sucesso */ }
 *     is MenuResult.Error -> { /* tratar erro */ }
 *     is MenuResult.Loading -> { /* mostrar loading */ }
 * }
 * ```
 */
sealed class CardapioResult<out T> {
    
    /**
     * Representa o estado de carregamento de dados.
     * Usado para indicar que uma operação está em andamento.
     */
    object Loading : CardapioResult<Nothing>()
    
    /**
     * Representa uma operação bem-sucedida.
     * 
     * @param data Os dados retornados pela operação
     */
    data class Success<T>(val data: T) : CardapioResult<T>()
    
    /**
     * Representa uma operação que falhou.
     * 
     * @param message Mensagem de erro amigável ao usuário
     * @param throwable Exceção original (opcional, para logging/debug)
     */
    data class Error(
        val message: String,
        val throwable: Throwable? = null
    ) : CardapioResult<Nothing>()

    val isSuccess: Boolean
        get() = this is Success

    val isError: Boolean
        get() = this is Error

    val isLoading: Boolean
        get() = this is Loading
}

