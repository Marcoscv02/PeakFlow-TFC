package marcos.peakflow.domain.model.user

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String? = null,
    val username: String?,
    val email: String?,
    //@Serializable(with = NullableLocalDateSerializer::class)
    val birthdate: LocalDate?,
    val gender: String?,
)


