package io.edanni.rndl.server.infrastructure.jooq.converter

import org.jooq.Converter
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import java.sql.Timestamp

class LocalDateTimeConverter : Converter<Timestamp, LocalDateTime> {
    override fun to(userObject: LocalDateTime?): Timestamp? {
        return if (userObject != null) {
            Timestamp(userObject.toInstant(ZoneOffset.UTC).toEpochMilli())
        } else {
            null
        }
    }

    override fun from(databaseObject: Timestamp?): LocalDateTime? {
        return if (databaseObject != null) {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(databaseObject.time), ZoneId.systemDefault())
        } else {
            null
        }
    }

    override fun fromType(): Class<Timestamp> = Timestamp::class.java

    override fun toType(): Class<LocalDateTime> = LocalDateTime::class.java
}