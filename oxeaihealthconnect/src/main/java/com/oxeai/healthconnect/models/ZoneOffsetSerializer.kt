package com.oxeai.healthconnect.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.ZoneOffset

object ZoneOffsetSerializer : KSerializer<ZoneOffset> {
    override val descriptor = PrimitiveSerialDescriptor("ZoneOffset", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ZoneOffset) {
        encoder.encodeString(value.id)
    }

    override fun deserialize(decoder: Decoder): ZoneOffset {
        return ZoneOffset.of(decoder.decodeString())
    }
}