package io.edanni.rndl.server.domain.service

import io.edanni.rndl.common.domain.entity.Entry
import io.edanni.rndl.common.domain.entity.Vehicle
import io.edanni.rndl.server.application.dto.TorqueEntryData
import io.edanni.rndl.server.domain.repository.EntryRepository
import io.edanni.rndl.server.domain.repository.TripRepository
import io.edanni.rndl.server.domain.repository.VehicleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Service
@Transactional
class EntryService(
        private val vehicleRepository: VehicleRepository,
        private val tripRepository: TripRepository,
        private val entryRepository: EntryRepository
) {
    //
    // CONTROLLER ENTRY POINTS
    //

    fun insertEntry(torqueId: String?, torqueEntryData: TorqueEntryData): Mono<Entry> =
            vehicleRepository.findIdByTorqueId(torqueId)
                    .flatMap { id -> tripRepository.findOrCreateTripIdByVehicleIdAndTimestamp(id, torqueEntryData.tripTimestamp!!) }
                    .flatMap { tripId -> entryRepository.insert(torqueEntryData.toEntry(tripId)) }

    //
    // INTERNAL
    //


    fun loadVehicle(torqueId: String?): Mono<Vehicle?> {
        return vehicleRepository.findByTorqueId(torqueId)
    }
}