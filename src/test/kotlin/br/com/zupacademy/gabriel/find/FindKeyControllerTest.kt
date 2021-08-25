package br.com.zupacademy.gabriel.find

import br.com.zupacademy.*
import br.com.zupacademy.gabriel.integration.KeyManagerGrpcFactory
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class FindKeyControllerTest {

    @field:Inject
    lateinit var clientGrpc: KeyManagerFindServiceGrpc.KeyManagerFindServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Test
    internal fun `deve encontrar e mostrar uma chave pix existente`() {
        //cenário
        val clientId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()
        val request = FindPixKeyRequest.newBuilder()
            .setPixId(
                FindPixKeyRequest.FilterByPixId.newBuilder()
                .setClientId(clientId)
                .setPixId(pixId)).build()
        val response = FindPixKeyResponse.newBuilder()
            .setPixId(pixId)
            .setClientId(clientId)
            .setTypeKey(KeyTypeRequest.EMAIL)
            .setKeyValue("gabriel@gmail.com")
            .setName("Gabriel")
            .setCpf("44719190839")
            .setAccount(AccountResponse.newBuilder().setNameInstitution("Itaú").setAgency("0001").setNumber("123456").setType(AccountType.CONTA_POUPANCA))
            .setCreatedIn(LocalDateTime.now().let {
                val createdIn = it.atZone(ZoneId.of("UTC")).toInstant()
                Timestamp.newBuilder()
                    .setSeconds(createdIn.epochSecond)
                    .setNanos(createdIn.nano)
                    .build()
            }).build()

        Mockito.`when`(clientGrpc.findKey(request)).thenReturn(response)
        //ação
        val httpResponse = httpClient.toBlocking()
            .exchange(HttpRequest.GET<Any>("/clients/$clientId/pix/keys/$pixId"), KeyDetailsResponse::class.java)

        //validação
        with(httpResponse) {
            assertEquals(HttpStatus.OK, status)
            assertEquals(clientId, body().clientId)
            assertNotNull(body())
        }
    }

}
@Factory
@Replaces(factory = KeyManagerGrpcFactory::class)
class FindStubFactory() {
    @Singleton
    fun findMock() = Mockito.mock(KeyManagerFindServiceGrpc.KeyManagerFindServiceBlockingStub::class.java)
}
