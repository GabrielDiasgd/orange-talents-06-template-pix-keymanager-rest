package br.com.zupacademy.gabriel.list

import br.com.zupacademy.ListPixKeyResponse
import br.com.zupacademy.gabriel.register.Account
import br.com.zupacademy.gabriel.register.KeyType
import java.time.LocalDateTime
import java.time.ZoneOffset

class ListKeyDetailsResponse(key: ListPixKeyResponse.ListPixKeyDetails) {
    val pixId = key.pixId
    val clientId = key.clientId
    val keyType = KeyType.valueOf(key.keyType.name)
    val keyValue = key.keyValue
    val accountType = Account.valueOf(key.type.name)
    val createdIn = LocalDateTime.ofEpochSecond(key.createdIn.seconds, key.createdIn.nanos, ZoneOffset.UTC)
}
