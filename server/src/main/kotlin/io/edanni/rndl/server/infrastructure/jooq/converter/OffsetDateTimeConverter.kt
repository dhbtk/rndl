package io.edanni.rndl.server.infrastructure.jooq.converter

import org.jooq.Converter
import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId

class OffsetDateTimeConverter : Converter<java.time.OffsetDateTime, OffsetDateTime> {
    override fun from(databaseObject: java.time.OffsetDateTime?): OffsetDateTime {
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli(databaseObject?.toInstant()?.toEpochMilli()!!), ZoneId.of(databaseObject.offset.id))
    }

    override fun to(userObject: OffsetDateTime?): java.time.OffsetDateTime {
        return java.time.OffsetDateTime.ofInstant(java.time.Instant.ofEpochMilli(userObject?.toInstant()?.toEpochMilli()!!), java.time.ZoneId.of(userObject.offset.id))
    }

    override fun fromType(): Class<java.time.OffsetDateTime> = java.time.OffsetDateTime::class.java

    override fun toType(): Class<OffsetDateTime> = OffsetDateTime::class.java
}