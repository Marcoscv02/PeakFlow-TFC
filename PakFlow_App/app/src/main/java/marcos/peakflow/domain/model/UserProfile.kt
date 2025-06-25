package marcos.peakflow.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable


@Serializable
data class UserProfile(
    val username: String,
    val birthdate: LocalDate? = null,
    val gender: String,
)
