package io.edanni.rndl.server.domain.repository

import io.edanni.rndl.common.domain.entity.*
import io.edanni.rndl.jooq.tables.Entry.ENTRY
import io.edanni.rndl.jooq.tables.Trip.TRIP
import io.edanni.rndl.jooq.tables.UserGroup.USER_GROUP
import io.edanni.rndl.jooq.tables.UserGroupMembership.USER_GROUP_MEMBERSHIP
import io.edanni.rndl.jooq.tables.Vehicle.VEHICLE
import io.edanni.rndl.server.infrastructure.mapping.recordToData
import io.edanni.rndl.server.infrastructure.repository.RecordNotFoundException
import org.jooq.DSLContext
import org.jooq.DatePart
import org.jooq.Field
import org.jooq.impl.DSL
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.temporal.TemporalAdjusters
import java.math.BigDecimal

@Repository
class TripRepository(private val create: DSLContext, private val jdbcTemplate: JdbcTemplate) {

    fun listTripsFiltered(year: Int, month: Int, user: User, vehicleId: Long?): List<Trip> {
        val monthStart = LocalDate.of(year, month, 1).with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay()
        val monthEnd = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59)
        val tripStart = DSL.field("to_timestamp(start_timestamp / 1000)") as Field<LocalDateTime>
        return create.select().from(TRIP)
                .innerJoin(VEHICLE).onKey()
                .innerJoin(USER_GROUP).onKey()
                .innerJoin(USER_GROUP_MEMBERSHIP).onKey()
                .where(TRIP.VEHICLE_ID.eq(vehicleId).or("?::bigint is null", vehicleId))
                .and(tripStart.between(monthStart, monthEnd))
                .and(USER_GROUP_MEMBERSHIP.USER_ID.eq(user.id))
                .orderBy(DSL.extract(tripStart, DatePart.DAY).desc(), TRIP.CREATED_AT)
                .fetch {
                    recordToData(it.into(TRIP), Trip::class).copy(
                            vehicle = recordToData(it.into(VEHICLE), Vehicle::class).copy(
                                    userGroup = recordToData(it.into(USER_GROUP), UserGroup::class)))
                }
    }

    fun findByIdAndUser(id: Long, user: User): Trip {
        val entries = create.selectFrom(ENTRY)
                .where(ENTRY.TRIP_ID.eq(id))
                .orderBy(ENTRY.DEVICE_TIME)
                .fetch { recordToData(it, Entry::class) }
        return create.select().from(TRIP)
                .innerJoin(VEHICLE).onKey()
                .innerJoin(USER_GROUP).onKey()
                .innerJoin(USER_GROUP_MEMBERSHIP).onKey()
                .where(TRIP.ID.eq(id))
                .and(USER_GROUP_MEMBERSHIP.USER_ID.eq(user.id))
                .fetchOptional()
                .map {
                    recordToData(it.into(TRIP), Trip::class).copy(
                            vehicle = recordToData(it.into(VEHICLE), Vehicle::class).copy(
                                    userGroup = recordToData(it.into(USER_GROUP), UserGroup::class)
                            )
                    )
                }
                .map { it.copy(entries = entries) }
                .orElseThrow { RecordNotFoundException(Trip::class, id) }
    }

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
                .set(t.DISTANCE, c.select(DSL.field("st_length(st_makeline(coordinates::geometry)::geography)").cast(Double::class.java))
                        .from(c.select(e.COORDINATES).from(ENTRY)
                        .where(e.TRIP_ID.eq(t.ID))
                        .and(e.COORDINATES.isNotNull)
                                .orderBy(e.DEVICE_TIME)))
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