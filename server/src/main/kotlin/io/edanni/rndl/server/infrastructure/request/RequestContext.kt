package io.edanni.rndl.server.infrastructure.request

import io.edanni.rndl.common.domain.entity.User
import org.springframework.security.core.context.ReactiveSecurityContextHolder

fun currentUser(): User = ReactiveSecurityContextHolder.getContext().map { it.authentication.principal as User }.block()!!