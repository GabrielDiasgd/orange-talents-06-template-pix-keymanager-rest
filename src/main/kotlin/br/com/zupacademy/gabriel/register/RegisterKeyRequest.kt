package br.com.zupacademy.gabriel.register


import br.com.zupacademy.AccountType
import br.com.zupacademy.KeyTypeRequest
import br.com.zupacademy.PixKeyRegistrationRequest
import br.com.zupacademy.gabriel.shared.validations.ValidPixKey
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull


@ValidPixKey
@Introspected
data class RegisterKeyRequest(
    @field:NotNull
    val keyType: KeyType,
    val keyValue: String?,
    @field:NotNull
    val accountType: Account
) {

    fun toPixKeyRegisterRequest(clientId: String): PixKeyRegistrationRequest {

        return PixKeyRegistrationRequest.newBuilder()
            .setClientId(clientId)
            .setKeyType(KeyTypeRequest.valueOf(this.keyType.name))
            .setKeyValue(keyValue ?: "")
            .setAccount(AccountType.valueOf(this.accountType.name))
            .build()
    }
}
