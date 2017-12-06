package io.edanni.rndl.server.domain.repository

import io.edanni.rndl.common.domain.entity.Entry
import io.edanni.rndl.common.domain.entity.Vehicle
import io.edanni.rndl.jooq.tables.Entry.ENTRY
import io.edanni.rndl.jooq.tables.Trip.TRIP
import io.edanni.rndl.jooq.tables.Vehicle.VEHICLE
import io.edanni.rndl.jooq.tables.records.VehicleRecord
import io.edanni.rndl.server.infrastructure.mapping.beanToData
import io.edanni.rndl.server.infrastructure.pagination.Page
import io.edanni.rndl.server.infrastructure.pagination.PageRequest
import io.edanni.rndl.server.infrastructure.repository.RecordNotFoundException
import org.jooq.DSLContext
import org.jooq.SelectConditionStep
import org.jooq.SelectWhereStep
import org.springframework.stereotype.Repository

@Repository
class VehicleRepository(private val create: DSLContext) {

    fun findAllWithLatestEntry(filter: String?, page: PageRequest): Page<Vehicle> {
        val usedFilter = filter ?: ""
        val where: (select: SelectWhereStep<*>) -> SelectConditionStep<*> = {
            it.where(VEHICLE.NAME.likeIgnoreCase("%$usedFilter%"))
        }

        val count = where(create.selectCount().from(VEHICLE)).execute()

        val vehicles = if (page.page != null) {
            val result = where(create.selectFrom(VEHICLE)).orderBy(VEHICLE.NAME).limit(page.size).offset(page.size * page.page)
                    .fetch { beanToData(it as VehicleRecord, Vehicle::class) }
            Page(count, page.page, result.size, count / page.size, result)
        } else {
            val result = where(create.selectFrom(VEHICLE)).orderBy(VEHICLE.NAME).fetch { beanToData(it as VehicleRecord, Vehicle::class) }
            Page(count, 0, count, 1, result)
        }

        val ids = vehicles.content.map { it.id!! }

        val entries = create.select().from(ENTRY)
                .join(TRIP).onKey()
                .join(VEHICLE).onKey()
                .where(VEHICLE.ID.`in`(ids))
                .fetch { record ->
                    val entryRecord = record.into(ENTRY)
                    entryRecord.id to beanToData(entryRecord, Entry::class)
                }.toMap()

        return vehicles.copy(content = vehicles.content.map { it.copy(latestEntry = entries[it.id]) })
    }

    fun findById(id: Long): Vehicle {
        return create.selectFrom(VEHICLE)
                .where(VEHICLE.ID.eq(id))
                .fetchOptional()
                .map { beanToData(it, Vehicle::class) }
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