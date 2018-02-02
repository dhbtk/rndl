package io.edanni.rndl.server.application.controller.advice

import io.edanni.rndl.server.infrastructure.repository.RecordNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ServerWebExchange

@RestControllerAdvice
class ControllerErrorHandler {
    @ExceptionHandler(RecordNotFoundException::class)
    fun handle404(exception: RecordNotFoundException, exchange: ServerWebExchange) {
        exchange.response.statusCode = HttpStatus.NOT_FOUND
    }
}