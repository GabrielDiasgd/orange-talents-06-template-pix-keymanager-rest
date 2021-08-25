package br.com.zupacademy.gabriel.shared.exceptionhandler

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.MessageSource
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.server.exceptions.ExceptionHandler
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class ApplicationExceptionHandler() : ExceptionHandler<StatusRuntimeException, HttpResponse<Any>> {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun handle(request: HttpRequest<*>?, exception: StatusRuntimeException): HttpResponse<Any> {
        val httpStatus = when (exception.status.code) {
                Status.ALREADY_EXISTS.code -> HttpStatus.UNPROCESSABLE_ENTITY
                Status.FAILED_PRECONDITION.code -> HttpStatus.UNPROCESSABLE_ENTITY
                Status.INVALID_ARGUMENT.code -> HttpStatus.BAD_REQUEST
                Status.NOT_FOUND.code -> HttpStatus.NOT_FOUND
                else -> {
                    logger.error("Ocorreu um erro inesperado na ${exception.javaClass.name} ao processar requisição", exception)
                    HttpStatus.INTERNAL_SERVER_ERROR
                }
            }
        logger.info("Status: $httpStatus, Causa: ${exception.status.code.name}, Mensagem: ${exception.status.description.toString()}")
        return HttpResponse.status<Any>(httpStatus)
            .body(ErrorMessage(exception.status.code.name, exception.status.description.toString()))
    }
    }
