package io.edanni.rndl.server.infrastructure.jooq.converter

import org.jooq.Converter
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneOffset
import java.sql.Time

class LocalTimeConverter : Converter<Time, LocalTime> {
    override fun from(time: Time?): LocalTime? {
        return if (time != null) {
            LocalTime.of(time.hours, time.minutes, time.seconds)
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