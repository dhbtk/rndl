package io.edanni.rndl.server.domain.repository

import io.edanni.rndl.common.domain.entity.Entry
import io.edanni.rndl.common.domain.entity.User
import io.edanni.rndl.common.domain.entity.UserGroup
import io.edanni.rndl.common.domain.entity.Vehicle
import io.edanni.rndl.jooq.tables.Entry.ENTRY
import io.edanni.rndl.jooq.tables.Trip.TRIP
import io.edanni.rndl.jooq.tables.UserGroup.USER_GROUP
import io.edanni.rndl.jooq.tables.UserGroupMembership.USER_GROUP_MEMBERSHIP
import io.edanni.rndl.jooq.tables.Vehicle.VEHICLE
import io.edanni.rndl.jooq.tables.records.VehicleRecord
import io.edanni.rndl.server.infrastructure.mapping.recordToData
import io.edanni.rndl.server.infrastructure.pagination.Page
import io.edanni.rndl.server.infrastructure.pagination.PageRequest
import io.edanni.rndl.server.infrastructure.repository.RecordNotFoundException
import org.jooq.DSLContext
import org.jooq.SelectConditionStep
import org.jooq.SelectWhereStep
import org.springframework.stereotype.Repository

@Repository
class VehicleRepository(private val create: DSLContext) {

    fun findAllWithLatestEntry(filter: String?, page: PageRequest, user: User): Page<Vehicle> {
        val usedFilter = filter ?: ""
        val where: (select: SelectWhereStep<*>) -> SelectConditionStep<*> = {
            it.where(VEHICLE.NAME.likeIgnoreCase("%$usedFilter%"))
                    .and(VEHICLE.USER_GROUP_ID.`in`(
                            create.select(USER_GROUP.ID)
                                    .from(USER_GROUP_MEMBERSHIP)
                                    .innerJoin(USER_GROUP).onKey()
                                    .where(USER_GROUP_MEMBERSHIP.USER_ID.eq(user.id))))
        }

        val count = where(create.selectCount().from(VEHICLE)).execute()

        val vehicles = if (page.page != null) {
            val result = where(create
                    .select().from(VEHICLE)
                    .innerJoin(USER_GROUP).onKey()).orderBy(VEHICLE.NAME).limit(page.size).offset(page.size * page.page)
                    .fetch { recordToData(it.into(VEHICLE) as VehicleRecord, Vehicle::class).copy(userGroup = recordToData(it.into(USER_GROUP), UserGroup::class)) }
            Page(count, page.page, result.size, count / page.size, result)
        } else {
            val result = where(create.selectFrom(VEHICLE)).orderBy(VEHICLE.NAME).fetch { recordToData(it as VehicleRecord, Vehicle::class) }
            Page(count, 0, count, 1, result)
        }

        val entries = vehicles.content
                .map { it.id }
                .map { id ->
                    create.select().from(ENTRY)
                            .innerJoin(TRIP).onKey()
                            .where(TRIP.VEHICLE_ID.eq(id))
                            .and(ENTRY.COORDINATES.isNotNull)
                            .orderBy(ENTRY.DEVICE_TIME.desc())
                            .limit(1)
                            .fetchOptional()
                            .map { id to recordToData(it.into(ENTRY), Entry::class) }
                }
                .filter { it.isPresent }
                .map { it.get() }.toMap()

        return vehicles.copy(content = vehicles.content.map { it.copy(latestEntry = entries[it.id]) })
    }

    fun findByIdAndUser(id: Long, user: User): Vehicle {
        return create.select()
                .from(VEHICLE)
                .innerJoin(USER_GROUP).onKey()
                .innerJoin(USER_GROUP_MEMBERSHIP).onKey()
                .leftJoin(TRIP).onKey()
                .leftJoin(ENTRY).on(ENTRY.ID.eq(
                create.select(ENTRY.ID)
                        .from(ENTRY).join(TRIP).onKey()
                        .where(TRIP.VEHICLE_ID.eq(VEHICLE.ID)).and(ENTRY.COORDINATES.isNotNull).orderBy(ENTRY.DEVICE_TIME.desc()).limit(1)))
                .where(VEHICLE.ID.eq(id))
                .and(USER_GROUP_MEMBERSHIP.USER_ID.eq(user.id))
                .fetchOptional()
                .map {
                    recordToData(it.into(VEHICLE), Vehicle::class).copy(
                            latestEntry = recordToData(it.into(ENTRY), Entry::class),
                            userGroup = recordToData(it.into(USER_GROUP), UserGroup::class)
                    )
                }
                .orElseThrow { RecordNotFoundException(Vehicle::class, id) }
    }

    fun findById(id: Long): Vehicle {
        return create.select()
                .from(VEHICLE)
                .leftJoin(TRIP).onKey()
                .leftJoin(ENTRY).on(ENTRY.ID.eq(
                create.select(ENTRY.ID)
                        .from(ENTRY).join(TRIP).onKey()
                        .where(TRIP.VEHICLE_ID.eq(VEHICLE.ID)).and(ENTRY.COORDINATES.isNotNull).orderBy(ENTRY.DEVICE_TIME.desc()).limit(1)))
                .where(VEHICLE.ID.eq(id))
                .fetchOptional()
                .map { recordToData(it.into(VEHICLE), Vehicle::class).copy(latestEntry = recordToData(it.into(ENTRY), Entry::class)) }
                .orElseThrow { RecordNotFoundException(Vehicle::class, id) }
    }

    fun findIdByTorqueId(torqueId: String?): Long {
        return create.select(VEHICLE.ID).from(VEHICLE)
                .where(VEHICLE.TORQUE_ID.eq(torqueId))
                .fetchOptional()
                .map { (id) -> id }
                .orElseThrow { RecordNotFoundException(Vehicle::class, "torqueId", torqueId) }
    }
}