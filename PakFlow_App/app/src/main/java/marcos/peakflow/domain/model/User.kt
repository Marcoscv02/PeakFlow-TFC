package marcos.peakflow.domain.model

import kotlinx.datetime.LocalDate
import java.util.UUID



data class User(
    val uuid: UUID,
    val username: String,
    val email: String,
    val birthdate: LocalDate?,
    val gender: String,
)
