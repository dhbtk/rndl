package io.edanni.rndl.server.infrastructure.jooq.converter

import org.jooq.Converter
import org.threeten.bp.*
import java.sql.Time

class LocalTimeConverter : Converter<Time, LocalTime> {
    override fun from(databaseObject: Time?): LocalTime =
            LocalDateTime.from(Instant.ofEpochMilli(databaseObject!!.time)).toLocalTime()

    override fun to(userObject: LocalTime?): Time = Time(userObject!!.atDate(LocalDate.of(1970, 1, 1)).toInstant(ZoneOffset.UTC).toEpochMilli())

    override fun toType(): Class<LocalTime> = LocalTime::class.java

    override fun fromType(): Class<Time> = Time::class.java
}