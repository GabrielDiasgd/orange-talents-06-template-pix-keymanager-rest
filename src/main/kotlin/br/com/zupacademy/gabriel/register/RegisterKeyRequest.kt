package br.com.zupacademy.gabriel.register


import br.com.zupacademy.AccountType
import br.com.zupacademy.KeyTypeRequest
import br.com.zupacademy.PixKeyRegistrationRequest
import br.com.zupacademy.gabriel.shared.validations.ValidPixKey
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


@ValidPixKey
@Introspected
data class RegisterKeyRequest(
    @field:NotNull
    val keyType: KeyType,
    @field:Size(max = 72)
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
