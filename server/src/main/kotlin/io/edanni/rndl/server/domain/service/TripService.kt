package io.edanni.rndl.server.domain.service

import io.edanni.rndl.server.domain.repository.TripRepository
import org.springframework.stereotype.Service

@Service
class TripService(private val tripRepository: TripRepository) {
    //
    // CONTROLLER ENTRY POINTS
    //

    fun listTripsByMonthAndVehicleId(year: Int, month: Int, vehicleId: Long?) = tripRepository.listTripsByMonthAndVehicleId(year, month, vehicleId)
}