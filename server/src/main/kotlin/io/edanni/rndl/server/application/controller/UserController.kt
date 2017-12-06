package io.edanni.rndl.server.application.controller

import io.edanni.rndl.common.domain.entity.User
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/user")
class UserController {

    @GetMapping
    fun show(): Mono<User> = ReactiveSecurityContextHolder.getContext().map { it.authentication.principal as User }
}