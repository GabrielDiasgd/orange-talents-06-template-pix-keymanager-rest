package br.com.zupacademy.gabriel.list

import br.com.zupacademy.*
import br.com.zupacademy.gabriel.integration.KeyManagerGrpcFactory
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class ListKeyControllerTest(){

    @field:Inject
    lateinit var clientGrpc: KeyManagerListServiceGrpc.KeyManagerListServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Test
    internal fun `deve retornar uma lista de chaves quando chave existente`() {
        //cenário
        val clientId = UUID.randomUUID().toString()
        val request = ListPixKeyRequest.newBuilder().setClientId(clientId).build()
        val response = ListPixKeyResponse.newBuilder().addAllListPixKey(listPixKey(clientId)).build()
        Mockito.`when`(clientGrpc.listKeys(request)).thenReturn(response)
        //ação
        val httpResponse = httpClient.toBlocking()
            .exchange(HttpRequest.GET<Any>("/clients/$clientId/pix/keys"), Argument.listOf(ListKeyDetailsResponse::class.java))

        //validação
        with(httpResponse) {
            assertEquals(HttpStatus.OK, status)
            assertNotNull(body())
            assertEquals(2, body().size)
        }
    }

    @Test
    internal fun `deve retornar uma lista vazia se cliente nao possuir chave ou informado cliente incorreto`() {
        //cenário
        val clientId = "ClienteIncorreto"
        val request = ListPixKeyRequest.newBuilder().setClientId(clientId).build()
        val response = ListPixKeyResponse.newBuilder().defaultInstanceForType
        Mockito.`when`(clientGrpc.listKeys(request)).thenReturn(response)
        //ação
        val httpResponse = httpClient.toBlocking()
            .exchange(HttpRequest.GET<Any>("/clients/$clientId/pix/keys"), Argument.listOf(ListKeyDetailsResponse::class.java))

        //validação
        with(httpResponse) {
            assertEquals(HttpStatus.OK, status)
            assertEquals(0, body().size)
        }
    }
}
@Factory
@Replaces(factory = KeyManagerGrpcFactory::class)
class ListStubFactory(){
    @Singleton
    fun listMock() = Mockito.mock(KeyManagerListServiceGrpc.KeyManagerListServiceBlockingStub::class.java)
}

fun listPixKey(clientId: String): ArrayList<ListPixKeyResponse.ListPixKeyDetails> {
    return arrayListOf(ListPixKeyResponse.ListPixKeyDetails.newBuilder()
            .setPixId(UUID.randomUUID().toString())
            .setClientId(clientId)
            .setKeyType(KeyTypeRequest.RANDOM)
            .setKeyValue(UUID.randomUUID().toString())
            .setType(AccountType.CONTA_CORRENTE)
            .setCreatedIn(Timestamp.getDefaultInstance())
            .build(),
        ListPixKeyResponse.ListPixKeyDetails.newBuilder()
            .setPixId(UUID.randomUUID().toString())
            .setClientId(clientId)
            .setKeyType(KeyTypeRequest.CPF)
            .setKeyValue("44719190839")
            .setType(AccountType.CONTA_CORRENTE)
            .setCreatedIn(Timestamp.getDefaultInstance())
            .build())

}