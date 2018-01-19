package io.edanni.rndl.server.domain.service

import io.edanni.rndl.common.domain.entity.User
import io.edanni.rndl.server.domain.repository.VehicleRepository
import io.edanni.rndl.server.infrastructure.pagination.PageRequest
import org.springframework.stereotype.Service

@Service
class VehicleService(private val vehicleRepository: VehicleRepository) {
    //
    // CONTROLLER ENTRY POINTS
    //

    fun findAllWithLatestEntry(filter: String?, pageRequest: PageRequest, user: User) = vehicleRepository.findAllWithLatestEntry(filter, pageRequest, user)

    fun findById(id: Long, user: User) = vehicleRepository.findByIdAndUser(id, user)
}