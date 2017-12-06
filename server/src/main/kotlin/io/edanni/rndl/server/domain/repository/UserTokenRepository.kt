package io.edanni.rndl.server.domain.repository

import io.edanni.rndl.common.domain.entity.User
import io.edanni.rndl.jooq.tables.UserToken.USER_TOKEN
import io.edanni.rndl.server.infrastructure.jwt.JwtTokenRepository
import org.jooq.DSLContext
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Repository
import org.threeten.bp.OffsetDateTime
import reactor.core.publisher.Mono

@Repository
class UserTokenRepository(private val create: DSLContext) : JwtTokenRepository {
    override fun deleteUserTokenByToken(token: String): Mono<Void> {
        return Mono.fromCallable {
            create.deleteFrom(USER_TOKEN)
                    .where(USER_TOKEN.TOKEN.eq(token))
                    .execute()
            null
        }
    }

    override fun insertUserTokenByToken(token: String, expiration: OffsetDateTime, user: UserDetails): Mono<Void> {
        val t = USER_TOKEN
        return Mono.fromCallable {
            create.insertInto(USER_TOKEN)
                    .columns(t.TOKEN, t.EXPIRES_AT, t.USER_ID)
                    .values(token, expiration, (user as User).id)
                    .execute()
            null
        }
    }
}