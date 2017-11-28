package io.edanni.rndl.server.application.controller

import io.edanni.rndl.server.application.dto.TorqueEntryData
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/upload")
class TorqueUploadController {

    @GetMapping
    fun upload(data: TorqueEntryData): Mono<String> {
        return Mono.just("OK!")
    }
}