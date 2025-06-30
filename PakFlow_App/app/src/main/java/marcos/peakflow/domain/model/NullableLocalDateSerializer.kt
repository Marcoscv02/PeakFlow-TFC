package marcos.peakflow.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.format.DateTimeFormatter


object NullableLocalDateSerializer : KSerializer<LocalDate?> {
    private val formatter = DateTimeFormatter.ISO_DATE
    override val descriptor = PrimitiveSerialDescriptor("LocalDate?", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDate?) {
//        encoder.encodeString(value?.format(formatter) ?: "") // Envía "" si es null
    }

    override fun deserialize(decoder: Decoder): LocalDate? {
        val dateString = decoder.decodeString()
//        return if (dateString.isBlank()) null else LocalDate.parse(dateString, formatter)
    }
}