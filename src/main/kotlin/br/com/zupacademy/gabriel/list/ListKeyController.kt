package br.com.zupacademy.gabriel.list

import br.com.zupacademy.KeyManagerListServiceGrpc
import br.com.zupacademy.ListPixKeyRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import javax.inject.Inject

@Controller("/clients/{clientId}/pix/keys")
class ListKeyController(@Inject val clientGrpc: KeyManagerListServiceGrpc.KeyManagerListServiceBlockingStub) {

    @Get
    fun list (@PathVariable clientId: String): HttpResponse<List<ListKeyDetailsResponse>> {
        val request = ListPixKeyRequest.newBuilder().setClientId(clientId).build()
        val response = clientGrpc.listKeys(request)

        val listKeyDetails = response.listPixKeyList.map { ListKeyDetailsResponse(it)}
        return HttpResponse.ok(listKeyDetails)
    }
}