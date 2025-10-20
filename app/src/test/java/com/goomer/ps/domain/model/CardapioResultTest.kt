package com.goomer.ps.domain.model

import org.junit.Assert.*
import org.junit.Test

class CardapioResultTest {

    @Test
    fun `Loading deve ter propriedades corretas`() {
        // Given
        val loading = CardapioResult.Loading()

        // Then
        assertTrue(loading.isLoading)
        assertFalse(loading.isSuccess)
        assertFalse(loading.isFailure)
        assertNull(loading.content)
    }

    @Test
    fun `Success deve ter propriedades corretas`() {
        // Given
        val data = listOf(1, 2, 3)
        val success = CardapioResult.Success(data)

        // Then
        assertFalse(success.isLoading)
        assertTrue(success.isSuccess)
        assertFalse(success.isFailure)
        assertEquals(data, success.content)
        assertEquals(data, success.value)
    }

    @Test
    fun `Failure deve ter propriedades corretas`() {
        // Given
        val exception = Exception("Erro de teste")
        val failure = CardapioResult.Failure(exception)

        // Then
        assertFalse(failure.isLoading)
        assertFalse(failure.isSuccess)
        assertTrue(failure.isFailure)
        assertNull(failure.content)
        assertEquals(exception, failure.throwable)
    }

    @Test
    fun `Failure sem throwable deve funcionar`() {
        // Given
        val failure = CardapioResult.Failure()

        // Then
        assertTrue(failure.isFailure)
        assertNull(failure.throwable)
    }

    @Test
    fun `getOrNull deve retornar valor para Success`() {
        // Given
        val data = "teste"
        val success = CardapioResult.Success(data)

        // When
        val result = success.getOrNull()

        // Then
        assertEquals(data, result)
    }

    @Test
    fun `getOrNull deve retornar null para Loading`() {
        // Given
        val loading = CardapioResult.Loading()

        // When
        val result = loading.getOrNull()

        // Then
        assertNull(result)
    }

    @Test
    fun `getOrNull deve retornar null para Failure`() {
        // Given
        val failure = CardapioResult.Failure(Exception())

        // When
        val result = failure.getOrNull()

        // Then
        assertNull(result)
    }

    @Test
    fun `throwableOrNull deve retornar throwable para Failure`() {
        // Given
        val exception = Exception("Erro")
        val failure = CardapioResult.Failure(exception)

        // When
        val result = failure.throwableOrNull()

        // Then
        assertEquals(exception, result)
    }

    @Test
    fun `throwableOrNull deve retornar null para Success`() {
        // Given
        val success = CardapioResult.Success("teste")

        // When
        val result = success.throwableOrNull()

        // Then
        assertNull(result)
    }

    @Test
    fun `throwableOrNull deve retornar null para Loading`() {
        // Given
        val loading = CardapioResult.Loading()

        // When
        val result = loading.throwableOrNull()

        // Then
        assertNull(result)
    }

    @Test
    fun `onResult deve retornar Success quando bloco executa com sucesso`() {
        // When
        val result = onResult { "sucesso" }

        // Then
        assertTrue(result is CardapioResult.Success)
        assertEquals("sucesso", (result as CardapioResult.Success).value)
    }

    @Test
    fun `onResult deve retornar Failure quando bloco lança exceção`() {
        // When
        val result = onResult { throw Exception("Erro") }

        // Then
        assertTrue(result is CardapioResult.Failure)
        assertEquals("Erro", (result as CardapioResult.Failure).throwable?.message)
    }

    @Test
    fun `onResult deve retornar Failure quando bloco retorna null`() {
        // When
        val result = onResult { null }

        // Then
        assertTrue(result is CardapioResult.Failure)
    }

    @Test
    fun `onResultSuccess deve executar bloco para Success`() {
        // Given
        val success = CardapioResult.Success("teste")
        var executed = false

        // When
        val result = success.onResultSuccess { executed = true }

        // Then
        assertTrue(executed)
        assertTrue(result is CardapioResult.Success)
    }

    @Test
    fun `onResultSuccess não deve executar bloco para Loading`() {
        // Given
        val loading = CardapioResult.Loading()
        var executed = false

        // When
        val result = loading.onResultSuccess { executed = true }

        // Then
        assertFalse(executed)
        assertTrue(result is CardapioResult.Loading)
    }

    @Test
    fun `onResultSuccess não deve executar bloco para Failure`() {
        // Given
        val failure = CardapioResult.Failure(Exception())
        var executed = false

        // When
        val result = failure.onResultSuccess { executed = true }

        // Then
        assertFalse(executed)
        assertTrue(result is CardapioResult.Failure)
    }

    @Test
    fun `onResultSuccess deve retornar Failure quando bloco lança exceção`() {
        // Given
        val success = CardapioResult.Success("teste")

        // When
        val result = success.onResultSuccess { throw Exception("Erro no bloco") }

        // Then
        assertTrue(result is CardapioResult.Failure)
        assertEquals("Erro no bloco", (result as CardapioResult.Failure).throwable?.message)
    }

    @Test
    fun `onResultFailure deve executar bloco para Failure`() {
        // Given
        val exception = Exception("Erro")
        val failure = CardapioResult.Failure(exception)
        var executed = false
        var capturedException: Throwable? = null

        // When
        val result = failure.onResultFailure { e ->
            executed = true
            capturedException = e
        }

        // Then
        assertTrue(executed)
        assertEquals(exception, capturedException)
        assertTrue(result is CardapioResult.Failure)
    }

    @Test
    fun `onResultFailure não deve executar bloco para Success`() {
        // Given
        val success = CardapioResult.Success("teste")
        var executed = false

        // When
        val result = success.onResultFailure { executed = true }

        // Then
        assertFalse(executed)
        assertTrue(result is CardapioResult.Success)
    }

    @Test
    fun `onResultFailure não deve executar bloco para Loading`() {
        // Given
        val loading = CardapioResult.Loading()
        var executed = false

        // When
        val result = loading.onResultFailure { executed = true }

        // Then
        assertFalse(executed)
        assertTrue(result is CardapioResult.Loading)
    }

    @Test
    fun `Success deve ser igual quando valores são iguais`() {
        // Given
        val data = listOf(1, 2, 3)
        val success1 = CardapioResult.Success(data)
        val success2 = CardapioResult.Success(data)

        // Then
        assertEquals(success1, success2)
    }

    @Test
    fun `Failure deve ser igual quando throwables são iguais`() {
        // Given
        val exception = Exception("Erro")
        val failure1 = CardapioResult.Failure(exception)
        val failure2 = CardapioResult.Failure(exception)

        // Then
        assertEquals(failure1, failure2)
    }

    @Test
    fun `Loading deve ser igual independente da instância`() {
        // Given
        val loading1 = CardapioResult.Loading()
        val loading2 = CardapioResult.Loading()

        // Then
        assertEquals(loading1, loading2)
    }
}
