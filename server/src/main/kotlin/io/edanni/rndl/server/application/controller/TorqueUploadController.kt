package io.edanni.rndl.server.application.controller

import io.edanni.rndl.server.application.dto.TorqueEntryData
import io.edanni.rndl.server.domain.service.EntryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

@RestController
@RequestMapping("/api/upload")
class TorqueUploadController(private val entryService: EntryService) {
    @GetMapping
    fun upload(data: TorqueEntryData): Mono<String> {
        entryService.insertEntry(data)
        return "OK!".toMono()
    }
}