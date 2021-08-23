package br.com.zupacademy.gabriel.shared.exceptionhandler

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ApplicationExceptionHandlerTest {

    val genericRequest = HttpRequest.GET<Any>("/")

    @Test
    internal fun `deve retornar 404 quando statusException for not found`() {

        val errorMessage = ErrorMessage(Status.NOT_FOUND.code.name, "Chave não encontrado")
        val notFoundException = StatusRuntimeException(Status.NOT_FOUND.withDescription(errorMessage.message))
        val response = ApplicationExceptionHandler().handle(genericRequest, notFoundException)

        assertEquals(HttpStatus.NOT_FOUND, response.status)
        assertNotNull(response.body())
        assertEquals(errorMessage, response.body())
    }

    @Test
    internal fun `deve retornar 422 quando statusException for already exists`() {

        val errorMessage = ErrorMessage(Status.ALREADY_EXISTS.code.name, "Chave já cadastrada")
        val alreadyExistsException = StatusRuntimeException(Status.ALREADY_EXISTS.withDescription(errorMessage.message))
        val response = ApplicationExceptionHandler().handle(genericRequest, alreadyExistsException)

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.status)
        assertNotNull(response.body())
        assertEquals(errorMessage, response.body())
    }

    @Test
    internal fun `deve retornar 400 quando statusException for invalid argument`() {
        val errorMessage = ErrorMessage(Status.INVALID_ARGUMENT.code.name, "Dados inválidos na requisição")
        val invalidArgumentException = StatusRuntimeException(Status.INVALID_ARGUMENT.withDescription(errorMessage.message))
        val response = ApplicationExceptionHandler().handle(genericRequest, invalidArgumentException)

        assertEquals(HttpStatus.BAD_REQUEST, response.status)
        assertNotNull(response.body())
        assertEquals(errorMessage, response.body())
    }

    @Test
    internal fun `deve retornar 422 quando statusException for failed precondition`() {
        val errorMessage = ErrorMessage(Status.FAILED_PRECONDITION.code.name, "Algum dado da requisição está incorreto, e o servidor grpc não completou a chamada")
        val failedPreconditionException = StatusRuntimeException(Status.FAILED_PRECONDITION.withDescription(errorMessage.message))
        val response = ApplicationExceptionHandler().handle(genericRequest, failedPreconditionException)

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.status)
        assertNotNull(response.body())
        assertEquals(errorMessage, response.body())
    }

    @Test
    internal fun `deve retornar 500 quando algum outro status for lancado`() {
        val errorMessage = ErrorMessage(Status.INTERNAL.code.name, "Erro interno")
        val internalException = StatusRuntimeException(Status.INTERNAL.withDescription(errorMessage.message))
        val response = ApplicationExceptionHandler().handle(genericRequest, internalException)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.status)
        assertNotNull(response.body())
        assertEquals(errorMessage, response.body())
    }
}