package io.edanni.rndl.server.infrastructure.jwt

import org.springframework.security.core.userdetails.UserDetails
import reactor.core.publisher.Mono

interface JwtUserRepository {
    /**
     * Loads an user from the given token, or an empty mono otherwise.
     */
    fun loadUserFromToken(token: String): Mono<UserDetails>
}