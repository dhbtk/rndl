package io.edanni.rndl.server.infrastructure.jwt

import io.edanni.rndl.common.domain.entity.User
import org.threeten.bp.OffsetDateTime
import reactor.core.publisher.Mono

interface JwtTokenRepository {
    fun deleteUserTokenByToken(token: String): Mono<Void>

    fun insertUserTokenByToken(token: String, expiration: OffsetDateTime, user: User): Mono<Void>
}