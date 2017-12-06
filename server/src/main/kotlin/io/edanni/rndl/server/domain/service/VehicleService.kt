package io.edanni.rndl.server.domain.service

import io.edanni.rndl.server.domain.repository.VehicleRepository
import io.edanni.rndl.server.infrastructure.pagination.PageRequest
import org.springframework.stereotype.Service

@Service
class VehicleService(private val vehicleRepository: VehicleRepository) {
    //
    // CONTROLLER ENTRY POINTS
    //

    fun findAllWithLatestEntry(filter: String?, pageRequest: PageRequest) = vehicleRepository.findAllWithLatestEntry(filter, pageRequest)

    fun findById(id: Long) = vehicleRepository.findById(id)
}