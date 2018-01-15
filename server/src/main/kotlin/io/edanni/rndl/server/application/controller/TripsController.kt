package io.edanni.rndl.server.application.controller

import io.edanni.rndl.common.domain.entity.Trip
import io.edanni.rndl.server.domain.service.TripService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/trips")
class TripsController(private val tripService: TripService) {

    @GetMapping
    fun index(year: Int, month: Int, vehicleId: Long?): Mono<List<Trip>> = Mono.fromCallable { tripService.listTripsByMonthAndVehicleId(year, month, vehicleId) }

    @GetMapping("/{id}")
    fun show(@PathVariable("id") id: Long): Mono<Trip> = Mono.fromCallable { tripService.findById(id) }
}