package io.edanni.rndl.server.infrastructure.jooq.converter

import org.jooq.Converter
import org.threeten.bp.*
import java.sql.Time

class LocalTimeConverter : Converter<Time, LocalTime> {
    override fun from(databaseObject: Time?): LocalTime? {
        return if (databaseObject != null) {
            LocalDateTime.from(Instant.ofEpochMilli(databaseObject.time)).toLocalTime()
        } else {
            null
        }
    }

    override fun to(userObject: LocalTime?): Time? {
        return if (userObject != null) {
            Time(userObject.atDate(LocalDate.of(1970, 1, 1)).toInstant(ZoneOffset.UTC).toEpochMilli())
        } else {
            null
        }
    }

    override fun toType(): Class<LocalTime> = LocalTime::class.java

    override fun fromType(): Class<Time> = Time::class.java
}