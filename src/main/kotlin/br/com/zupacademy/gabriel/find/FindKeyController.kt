package br.com.zupacademy.gabriel.find

import br.com.zupacademy.FindPixKeyRequest
import br.com.zupacademy.KeyManagerFindServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.inject.Inject

@Validated
@Controller("/clients/{clientId}/pix/keys")
class FindKeyController(@Inject val clientGrpc: KeyManagerFindServiceGrpc.KeyManagerFindServiceBlockingStub) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Get("{pixId}")
    fun findKey(@PathVariable clientId: String, @PathVariable pixId: String): HttpResponse<FindKeyResponse> {
        logger.info("Find key pix clientId: $clientId, pixId: $pixId")
        val request = FindPixKeyRequest.newBuilder()
            .setPixId(FindPixKeyRequest.FilterByPixId.newBuilder()
                .setClientId(clientId)
                .setPixId(pixId)).build()

        val response = clientGrpc.findKey(request)

        return HttpResponse.ok(FindKeyResponse(response))
    }
}
