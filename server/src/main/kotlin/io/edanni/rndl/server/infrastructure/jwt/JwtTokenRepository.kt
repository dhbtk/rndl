package io.edanni.rndl.server.infrastructure.jwt

import org.springframework.security.core.userdetails.UserDetails
import org.threeten.bp.OffsetDateTime
import reactor.core.publisher.Mono

interface JwtTokenRepository {
    fun deleteUserTokenByToken(token: String): Mono<Void>

    fun insertUserTokenByToken(token: String, expiration: OffsetDateTime, user: UserDetails): Mono<Void>
}