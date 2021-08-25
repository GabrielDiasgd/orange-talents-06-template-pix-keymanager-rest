package br.com.zupacademy.gabriel.find


import br.com.zupacademy.AccountType
import br.com.zupacademy.FindPixKeyResponse
import java.time.LocalDateTime
import java.time.ZoneOffset

data class KeyDetailsResponse(
    val pixId: String,
    val clientId: String,
    val keyType: String,
    val keyValue: String,
    val name: String,
    val cpf: String,
    val account: BankAccount,
    val createId: LocalDateTime
) {

    constructor(response: FindPixKeyResponse) : this(
        response.pixId,
        response.clientId,
        response.typeKey.name,
        response.keyValue,
        response.name,
        response.cpf,
        BankAccount(response.account.nameInstitution, response.account.agency, response.account.number,
            when(response.account.type){
                AccountType.CONTA_CORRENTE -> "CONTA_CORRENTE"
                AccountType.CONTA_POUPANCA -> "CONTA_POUPANCA"
                else -> "CONTA_DESCONHECIDA"
            }),
        LocalDateTime.ofEpochSecond(response.createdIn.seconds,response.createdIn.nanos, ZoneOffset.UTC)
    )
}

data class BankAccount(val nameInstitution: String, val agency: String, val number: String, val type: String)