package io.edanni.rndl.server.application.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/vehicles")
class VehiclesController {

    @GetMapping
    fun index() = "[]"
}