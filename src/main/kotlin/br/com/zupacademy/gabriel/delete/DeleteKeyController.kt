package br.com.zupacademy.gabriel.delete

import br.com.zupacademy.KeyManagerDeleteServiceGrpc
import br.com.zupacademy.PixKeyDeleteRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated
import javax.inject.Inject

@Validated
@Controller("/clients/{clientId}/pix/keys")
class DeleteKeyController(@Inject val clientGrpc: KeyManagerDeleteServiceGrpc.KeyManagerDeleteServiceBlockingStub) {

    @Delete("/{pixId}")
    fun delete (@PathVariable clientId: String, @PathVariable pixId: String): HttpResponse<Any> {
        clientGrpc.delete(
            PixKeyDeleteRequest.newBuilder()
            .setPixId(pixId)
            .setClientId(clientId)
            .build())

        return HttpResponse.ok()
    }
}