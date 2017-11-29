package io.edanni.rndl.server.domain.repository

import io.edanni.rndl.jooq.tables.Trip.TRIP
import org.jooq.DSLContext
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class TripRepository(private val create: DSLContext, private val jdbcTemplate: JdbcTemplate) {

    fun findOrCreateTripIdByVehicleIdAndTimestamp(vehicleId: Long, timestamp: Long): Long {
        jdbcTemplate.execute("LOCK trip IN ACCESS EXCLUSIVE MODE")
        return create.select(TRIP.ID).from(TRIP)
                .where(TRIP.VEHICLE_ID.eq(vehicleId)).and(TRIP.START_TIMESTAMP.eq(timestamp))
                .fetchOptional()
                .map { (id) -> id }
                .orElseGet {
                    create.insertInto(TRIP).columns(TRIP.VEHICLE_ID, TRIP.START_TIMESTAMP)
                            .values(vehicleId, timestamp)
                            .returning(TRIP.ID)
                            .fetchOptional()
                            .map { it.id }
                            .get()
                }
    }
}