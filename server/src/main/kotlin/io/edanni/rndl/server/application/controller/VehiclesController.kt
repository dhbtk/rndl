package io.edanni.rndl.server.application.controller

import io.edanni.rndl.common.domain.entity.Vehicle
import io.edanni.rndl.server.domain.service.VehicleService
import io.edanni.rndl.server.infrastructure.pagination.Page
import io.edanni.rndl.server.infrastructure.pagination.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

@RestController
@RequestMapping("/api/vehicles")
class VehiclesController(private val vehicleService: VehicleService) {

    @GetMapping
    fun index(filter: String?, pageRequest: PageRequest): Mono<Page<Vehicle>> = vehicleService.findAllWithLatestEntry(filter, pageRequest).toMono()

    @GetMapping("/{id}")
    fun show(@PathVariable("id") id: Long?): Mono<Vehicle> = vehicleService.findById(id!!).toMono()
}