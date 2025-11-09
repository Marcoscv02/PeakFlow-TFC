package marcos.peakflow.domain.model.user

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String? = null, // Es null antes de guardarse
    @SerialName("name")val username: String?,
    @Transient val email: String? = null,
    @Serializable(with = NullableLocalDateSerializer::class)
    val birthdate: LocalDate?,
    val gender: String?,
)


