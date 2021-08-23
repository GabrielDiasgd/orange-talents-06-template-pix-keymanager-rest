package br.com.zupacademy.gabriel.integration

import br.com.zupacademy.KeyManagerRegisterServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class ClientGrpc {

    @Singleton
    fun registerPixKeyClientGrpc(@GrpcChannel("pix") channel: ManagedChannel): KeyManagerRegisterServiceGrpc.KeyManagerRegisterServiceBlockingStub {
        return KeyManagerRegisterServiceGrpc.newBlockingStub(channel)
    }
}