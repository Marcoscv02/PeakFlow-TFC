package marcos.peakflow.domain.model.user

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String? = null, // Es null antes de guardarse
    val username: String?,
    val email: String?,
    @Serializable(with = NullableLocalDateSerializer::class)
    val birthdate: LocalDate?,
    val gender: String?,
)


