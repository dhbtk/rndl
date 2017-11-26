package io.edanni.rndl.server.infrastructure.jooq.converter

import org.jooq.Converter
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import java.sql.Timestamp

class LocalDateTimeConverter : Converter<Timestamp, LocalDateTime> {
    override fun to(userObject: LocalDateTime?): Timestamp = Timestamp(userObject!!.toInstant(ZoneOffset.UTC).toEpochMilli())

    override fun from(databaseObject: Timestamp?): LocalDateTime = LocalDateTime.from(Instant.ofEpochMilli(databaseObject!!.time))

    override fun fromType(): Class<Timestamp> = Timestamp::class.java

    override fun toType(): Class<LocalDateTime> = LocalDateTime::class.java
}