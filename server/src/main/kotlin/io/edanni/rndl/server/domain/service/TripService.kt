package io.edanni.rndl.server.domain.service

import io.edanni.rndl.common.domain.entity.User
import io.edanni.rndl.server.domain.repository.TripRepository
import org.springframework.stereotype.Service

@Service
class TripService(private val tripRepository: TripRepository) {
    //
    // CONTROLLER ENTRY POINTS
    //

    fun listTripsFiltered(year: Int, month: Int, user: User, vehicleId: Long?) = tripRepository.listTripsFiltered(year, month, user, vehicleId)

    fun findByIdAndUser(id: Long, user: User) = tripRepository.findByIdAndUser(id, user)
}