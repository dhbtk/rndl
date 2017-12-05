package io.edanni.rndl.server.domain.repository

import io.edanni.rndl.jooq.Routines
import io.edanni.rndl.jooq.tables.Entry.ENTRY
import io.edanni.rndl.jooq.tables.Trip.TRIP
import org.jooq.DSLContext
import org.jooq.util.postgres.PostgresDataType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.threeten.bp.LocalTime
import org.threeten.bp.OffsetDateTime
import java.math.BigDecimal

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

    fun updateCalculatedTripInfo() {
        val t = TRIP
        val e = ENTRY
        val c = create
        c.update(TRIP)
                .set(t.ECONOMY,
                        c.select(e.ECONOMY.cast(Double::class.java))
                                .from(ENTRY)
                                .where(e.TRIP_ID.eq(t.ID))
                                .and(e.RPM.gt(BigDecimal(0)))
                                .orderBy(e.DEVICE_TIME.desc())
                                .limit(1))
                .set(t.AVERAGE_SPEED,
                        c.select(e.SPEED.avg().cast(Int::class.java))
                                .from(ENTRY)
                                .where(e.TRIP_ID.eq(t.ID)))
                .set(t.MAXIMUM_SPEED,
                        c.select(e.SPEED.max().cast(Int::class.java))
                                .from(ENTRY)
                                .where(e.TRIP_ID.eq(t.ID)))
                .set(t.DURATION,
                        c.select(e.DEVICE_TIME.max().minus(e.DEVICE_TIME.min()).cast(LocalTime::class.java))
                                .from(ENTRY)
                                .where(e.TRIP_ID.eq(t.ID)))
                .set(t.DISTANCE, c.select(
                        Routines.stLength1(
                                Routines.stGeogfromtext(
                                        Routines.stMakeline3(e.COORDINATES).cast(PostgresDataType.TEXT)
                                )
                        ))
                        .from(ENTRY)
                        .where(e.TRIP_ID.eq(t.ID))
                        .and(e.COORDINATES.isNotNull)
                        .orderBy(e.DEVICE_TIME))
                .set(t.UPDATED_AT, OffsetDateTime.now())
                .where(t.ID.`in`(
                        c.select(t.ID)
                                .from(TRIP)
                                .where(t.UPDATED_AT.le(
                                        c.select(e.UPDATED_AT.max())
                                                .from(ENTRY)
                                                .where(e.TRIP_ID.eq(t.ID))))))
                .execute()
    }
}