package br.com.zupacademy.gabriel.delete

import br.com.zupacademy.KeyManagerDeleteServiceGrpc
import br.com.zupacademy.PixKeyDeleteResponse
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
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class DeleteKeyControllerTest {

    @field:Inject
    lateinit var clientGrpc: KeyManagerDeleteServiceGrpc.KeyManagerDeleteServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @BeforeEach
    internal fun setUp() {
        Mockito.reset(clientGrpc)
    }

    @Test
    internal fun `deve excluir chave existente`() {
        //cenário
        val clientId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()
        Mockito.`when`(clientGrpc.delete(Mockito.any())).thenReturn(PixKeyDeleteResponse.newBuilder().setResponse("Sucesso Teste").build())
        //ação
        val response = httpClient.toBlocking().exchange(HttpRequest.DELETE<Any>("/clients/$clientId/pix/keys/$pixId"), Any::class.java)
        //validação
        assertTrue(response.body.isEmpty)
        assertEquals(HttpStatus.OK, response.status)
    }

    @Test
    internal fun `nao deve excluir chave quando chave nao existente`() {
        val clientId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()
        Mockito.`when`(clientGrpc.delete(Mockito.any())).thenThrow(StatusRuntimeException(Status.NOT_FOUND.withDescription("Chave não encontrada")))
        //ação
        val error = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking()
                .exchange(HttpRequest.DELETE<Any>("/clients/$clientId/pix/keys/$pixId"), Any::class.java)
        }
        //validação
        with(error) {
            assertEquals(HttpStatus.NOT_FOUND, error.status)
        }
    }

    @Test
    internal fun `nao deve excluir chave quando chave nao pertence ao cliente`() {
        val clientId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()
        Mockito.`when`(clientGrpc.delete(Mockito.any())).thenThrow(StatusRuntimeException
            (Status.FAILED_PRECONDITION.withDescription("Chave não pertence ao cliente")))
        //ação
        val error = assertThrows<HttpClientResponseException> {
          httpClient.toBlocking()
                .exchange(HttpRequest.DELETE<Any>("/clients/$clientId/pix/keys/$pixId"), Any::class.java)
        }
        //validação
        with(error) {
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, error.status)

        }
    }

    @Test
    internal fun `nao deve excluir chave quando ocorrer erro no bcb`() {
        val clientId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()
        Mockito.`when`(clientGrpc.delete(Mockito.any())).thenThrow(StatusRuntimeException
            (Status.FAILED_PRECONDITION.withDescription("Não foi possível exclui chave no banco central")))
        //ação
        val error = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking()
                .exchange(HttpRequest.DELETE<Any>("/clients/$clientId/pix/keys/$pixId"), Any::class.java)
        }
        //validação
        with(error) {
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, error.status)
        }
    }
}



@Factory
@Replaces(factory = KeyManagerGrpcFactory::class)
internal class DeleteStubFactory() {
    @Singleton
    internal fun deleteMock() = Mockito.mock(KeyManagerDeleteServiceGrpc.KeyManagerDeleteServiceBlockingStub::class.java)
}