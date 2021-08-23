package br.com.zupacademy.gabriel.register

import br.com.zupacademy.KeyManagerRegisterServiceGrpc
import io.micronaut.discovery.ServiceInstance
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.uri.UriBuilder
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.validation.Validated
import org.hibernate.cfg.Environment
import javax.inject.Inject
import javax.validation.Valid

@Validated
@Controller("/clients/{clientId}/pix/keys")
class RegisterKeyController(
    @Inject private val clientGrpc: KeyManagerRegisterServiceGrpc.KeyManagerRegisterServiceBlockingStub,
    @Inject val embeddedServer: EmbeddedServer
) {

    @Post
    fun register(@PathVariable clientId: String, @Body @Valid registerKeyRequest: RegisterKeyRequest): HttpResponse<Any> {
        val grpcRequest = registerKeyRequest.toPixKeyRegisterRequest(clientId)
        val grpcResponse = clientGrpc.register(grpcRequest)

        val uri = UriBuilder.of("${embeddedServer.uri}/clients/{clientId}/pix/keys/{pixId}")
            .expand(mutableMapOf(Pair("pixId", grpcResponse.pixId), Pair("clientId", clientId)))

        return HttpResponse.created(RegisterKeyResponse(grpcResponse.pixId), uri)

    }
}
