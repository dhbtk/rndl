package io.edanni.rndl.server.infrastructure.request

import io.edanni.rndl.common.domain.entity.User
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import reactor.core.publisher.Mono

fun currentUser(): Mono<User> = ReactiveSecurityContextHolder.getContext().map { it.authentication.principal as User }