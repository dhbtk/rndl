package io.edanni.rndl.server.domain.repository

import io.edanni.rndl.jooq.tables.Trip.TRIP
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class TripRepository(private val create: DSLContext) {

    fun findOrCreateTripIdByVehicleIdAndTimestamp(vehicleId: Long, timestamp: Long): Mono<Long> {
        return Mono.fromCallable {
            create.select(TRIP.ID).from(TRIP)
                    .where(TRIP.VEHICLE_ID.eq(vehicleId)).and(TRIP.START_TIMESTAMP.eq(timestamp))
                    .fetchOptional()
                    .map { (id) -> id }
                    .orElseGet {
                        create.insertInto(TRIP).columns(TRIP.VEHICLE_ID, TRIP.START_TIMESTAMP)
                                .values(vehicleId, timestamp)
                                .returning(TRIP.ID)
                                .fetchOptional()
                                .map { it.id }
                                .orElseThrow { IllegalStateException() }
                    }
        }
    }
}