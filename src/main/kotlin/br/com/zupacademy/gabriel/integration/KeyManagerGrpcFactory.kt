package br.com.zupacademy.gabriel.integration

import br.com.zupacademy.KeyManagerDeleteServiceGrpc
import br.com.zupacademy.KeyManagerFindServiceGrpc
import br.com.zupacademy.KeyManagerListServiceGrpc
import br.com.zupacademy.KeyManagerRegisterServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class KeyManagerGrpcFactory {

    @Singleton
    fun registerPixKey(@GrpcChannel("pix") channel: ManagedChannel) = KeyManagerRegisterServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun deletePixKey(@GrpcChannel("pix") channel: ManagedChannel) = KeyManagerDeleteServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun findKey(@GrpcChannel("pix") channel: ManagedChannel) = KeyManagerFindServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun listKey(@GrpcChannel("pix") channel: ManagedChannel) = KeyManagerListServiceGrpc.newBlockingStub(channel)
}