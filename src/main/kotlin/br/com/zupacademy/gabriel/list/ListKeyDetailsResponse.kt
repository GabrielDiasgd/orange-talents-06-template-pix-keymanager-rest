package br.com.zupacademy.gabriel.list

import br.com.zupacademy.ListPixKeyResponse
import br.com.zupacademy.gabriel.register.Account
import br.com.zupacademy.gabriel.register.KeyType
import io.micronaut.core.annotation.Introspected
import java.time.LocalDateTime
import java.time.ZoneOffset

data class ListKeyDetailsResponse(
    val pixId: String,
    val clientId: String,
    val keyType: KeyType,
    val keyValue: String,
    val accountType: Account,
    val createdIn: LocalDateTime
) {

    constructor(key: ListPixKeyResponse.ListPixKeyDetails) : this(
        key.pixId,
        key.clientId,
        KeyType.valueOf(key.keyType.name),
        key.keyValue,
        Account.valueOf(key.type.name),
        LocalDateTime.ofEpochSecond(key.createdIn.seconds, key.createdIn.nanos, ZoneOffset.UTC)
    )
}
