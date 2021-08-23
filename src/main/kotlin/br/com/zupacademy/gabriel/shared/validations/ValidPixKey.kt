package br.com.zupacademy.gabriel.shared.validations

import br.com.zupacademy.gabriel.register.RegisterKeyRequest
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.inject.Singleton
import javax.validation.Constraint

@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidPixKeyValidator::class])
annotation class ValidPixKey(val message: String = "Tipo de chave é incompativel com valor informado")


@Singleton
class ValidPixKeyValidator: ConstraintValidator<ValidPixKey, RegisterKeyRequest> {

    override fun isValid(
        value: RegisterKeyRequest?,
        annotationMetadata: AnnotationValue<ValidPixKey>,
        context: ConstraintValidatorContext
    ): Boolean {
        if (value?.keyType == null) return false

        return value.keyType.validator(value.keyValue)
    }
}
