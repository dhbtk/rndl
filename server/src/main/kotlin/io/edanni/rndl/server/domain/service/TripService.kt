package io.edanni.rndl.server.domain.service

import io.edanni.rndl.server.domain.repository.TripRepository
import org.springframework.stereotype.Service

@Service
class TripService(private val tripRepository: TripRepository) {

}