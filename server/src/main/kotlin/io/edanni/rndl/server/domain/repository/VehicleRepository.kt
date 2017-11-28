package io.edanni.rndl.server.domain.repository

import io.edanni.rndl.common.domain.entity.Vehicle
import io.edanni.rndl.jooq.tables.Vehicle.VEHICLE
import io.edanni.rndl.server.infrastructure.mapping.beanToData
import io.edanni.rndl.server.infrastructure.repository.RecordNotFoundException
import org.jooq.DSLContext
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Repository
class VehicleRepository(private val create: DSLContext, private val modelMapper: ModelMapper) {

    fun findIdByTorqueId(torqueId: String?): Mono<Long> {
        return Mono.fromCallable {
            create.select(VEHICLE.ID).from(VEHICLE)
                    .where(VEHICLE.TORQUE_ID.eq(torqueId))
                    .fetchOptional()
                    .map { (id) -> id }
                    .orElseThrow { RecordNotFoundException(Vehicle::class, "torqueId", torqueId) }
        }.publishOn(Schedulers.elastic())
    }

    fun findByTorqueId(torqueId: String?): Mono<Vehicle?> {
        return Mono.fromCallable {
            create.selectFrom(VEHICLE)
                    .where(VEHICLE.TORQUE_ID.eq(torqueId))
                    .fetchOptional()
                    .map { beanToData<Vehicle>(it, Vehicle::class) }
                    .orElse(null)
        }.publishOn(Schedulers.elastic())
    }
}