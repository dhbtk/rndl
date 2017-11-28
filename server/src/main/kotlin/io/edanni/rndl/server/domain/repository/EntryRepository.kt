package io.edanni.rndl.server.domain.repository

import io.edanni.rndl.common.domain.entity.Entry
import io.edanni.rndl.jooq.Tables.ENTRY
import io.edanni.rndl.server.infrastructure.mapping.beanToData
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Repository
class EntryRepository(private val create: DSLContext) {

    fun insert(e: Entry): Mono<Entry> {
        val t = ENTRY
        return Mono.fromCallable {
            create.insertInto(ENTRY)
                    .columns(
                            t.TRIP_ID, t.DEVICE_TIME, t.COORDINATES, t.GPS_SPEED, t.GPS_ALTITUDE, t.RPM, t.ECONOMY,
                            t.SPEED, t.THROTTLE, t.INSTANT_ECONOMY, t.FUEL_FLOW, t.FUEL_USED)
                    .values(e.tripId, e.deviceTime, e.coordinates, e.gpsSpeed, e.gpsAltitude, e.rpm, e.economy,
                            e.speed, e.throttle, e.instantEconomy, e.fuelFlow, e.fuelUsed)
                    .returning()
                    .fetchOptional()
                    .map { beanToData<Entry>(it, Entry::class) }
                    .orElseThrow { IllegalStateException() }
        }.publishOn(Schedulers.elastic())
    }
}