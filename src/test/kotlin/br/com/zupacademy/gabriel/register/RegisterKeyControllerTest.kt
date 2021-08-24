package br.com.zupacademy.gabriel.register

import br.com.zupacademy.*
import br.com.zupacademy.gabriel.integration.KeyManagerGrpcFactory
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RegisterKeyControllerTest() {

    @field:Inject
    lateinit var clientGrpc: KeyManagerRegisterServiceGrpc.KeyManagerRegisterServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @BeforeEach
    internal fun setUp() {
        Mockito.reset(clientGrpc)
    }

    @Test
    fun `deve cadastrar nova chave quando dados corretos`() {
        //cenário
        val clientId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()
        val responseGrpc = PixKeyRegistrationResponse.newBuilder()
            .setClientId(clientId)
            .setPixId(pixId)
            .build()

        Mockito.`when`(clientGrpc.register(Mockito.any())).thenReturn(responseGrpc)

        val registerKeyRequest = RegisterKeyRequest(KeyType.EMAIL, "gabriel@gmail.com", Account.CONTA_CORRENTE)

        //ação
        val request = HttpRequest.POST("/clients/$clientId/pix/keys", registerKeyRequest)
        val response = httpClient.toBlocking().exchange(request, RegisterKeyResponse::class.java)


        //Validação
        with(response) {
           assertEquals(HttpStatus.CREATED, status)
            assertTrue(headers.contains("Location"))
            assertTrue(header("Location").contains(pixId))
        }
    }

    @Test
    internal fun `nao deve cadastrar chave quando cliente inexistente`() {
        val clientId = "Inexistente"
        val registerKeyRequest = RegisterKeyRequest(KeyType.RANDOM, "", Account.CONTA_POUPANCA)

        Mockito.`when`(clientGrpc.register(Mockito.any())).thenThrow(StatusRuntimeException
            (Status.FAILED_PRECONDITION.withDescription("Cliente não encontrado")))

        val error = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(HttpRequest.POST("/clients/$clientId/pix/keys", registerKeyRequest), registerKeyRequest::class.java)
        }

        with(error) {
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, status)
        }
    }
}

@Factory
@Replaces(factory = KeyManagerGrpcFactory::class)
internal class RegisterStubFactory() {
    @Singleton
    internal fun registerMock() = Mockito.mock(KeyManagerRegisterServiceGrpc.KeyManagerRegisterServiceBlockingStub::class.java)
}
