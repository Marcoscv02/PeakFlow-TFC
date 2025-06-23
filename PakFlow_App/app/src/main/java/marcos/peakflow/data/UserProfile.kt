package marcos.peakflow.data

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class UserProfile(
    val id: String,
    val username: String? = null,
    val full_name: String? = null,
    @Serializable(with = LocalDateSerializer::class)
    val birthdate: LocalDate? = null,
    val gender: String? = null,
    val created_at: String? = null
)
