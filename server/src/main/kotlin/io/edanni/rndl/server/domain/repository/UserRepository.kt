package io.edanni.rndl.server.domain.repository

import io.edanni.rndl.common.domain.entity.User
import io.edanni.rndl.jooq.tables.User.USER
import io.edanni.rndl.jooq.tables.UserToken.USER_TOKEN
import io.edanni.rndl.server.infrastructure.jwt.JwtUserRepository
import io.edanni.rndl.server.infrastructure.mapping.beanToData
import org.jooq.DSLContext
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Repository
import org.threeten.bp.OffsetDateTime
import reactor.core.publisher.Mono

@Repository
class UserRepository(private val create: DSLContext) : ReactiveUserDetailsService, JwtUserRepository {
    override fun findByUsername(username: String?): Mono<UserDetails> {
        return Mono.fromCallable {
            create.selectFrom(USER)
                    .where(USER.EMAIL.lower().eq(username?.toLowerCase()))
                    .fetchOptional()
                    .map { beanToData(it, User::class) as UserDetails }
                    .orElseThrow { UsernameNotFoundException(username) }
        }
    }

    override fun loadUserFromToken(token: String): Mono<UserDetails> {
        return Mono.fromCallable {
            create.select(
                    USER.ID,
                    USER.CREATED_AT,
                    USER.UPDATED_AT,
                    USER.EMAIL,
                    USER.PASSWORD_DIGEST,
                    USER.FULL_NAME)
                    .from(USER)
                    .innerJoin(USER_TOKEN)
                    .on(USER.ID.eq(USER_TOKEN.USER_ID))
                    .where(USER_TOKEN.TOKEN.eq(token))
                    .and(USER_TOKEN.EXPIRES_AT.gt(OffsetDateTime.now()))
                    .fetchOptional()
                    .map { beanToData(it, User::class) as UserDetails }
                    .orElse(null)
        }.filter { it != null }
    }
}