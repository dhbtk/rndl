package io.edanni.rndl.server.domain.repository

import io.edanni.rndl.common.domain.entity.User
import io.edanni.rndl.common.domain.entity.UserGroup
import io.edanni.rndl.common.domain.entity.UserGroupMembership
import io.edanni.rndl.jooq.tables.User.USER
import io.edanni.rndl.jooq.tables.UserGroup.USER_GROUP
import io.edanni.rndl.jooq.tables.UserGroupMembership.USER_GROUP_MEMBERSHIP
import io.edanni.rndl.jooq.tables.UserToken.USER_TOKEN
import io.edanni.rndl.server.infrastructure.jwt.JwtUserRepository
import io.edanni.rndl.server.infrastructure.mapping.recordToData
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
                    .map { recordToData(it, User::class) as UserDetails }
                    .orElseThrow { UsernameNotFoundException(username) }
        }
    }

    override fun loadUserFromToken(token: String): Mono<UserDetails> {
        return Mono.fromCallable {
            create.select()
                    .from(USER)
                    .innerJoin(USER_TOKEN).onKey()
                    .where(USER_TOKEN.TOKEN.eq(token))
                    .and(USER_TOKEN.EXPIRES_AT.gt(OffsetDateTime.now()))
                    .fetchOptional()
                    .map { recordToData(it.into(USER), User::class).copy(userGroupMemberships = loadMembershipsForUser(it.into(USER).id)) as UserDetails }
                    .orElse(null)
        }.filter { it != null }
    }

    private fun loadMembershipsForUser(userId: Long): List<UserGroupMembership> {
        return create.select()
                .from(USER_GROUP_MEMBERSHIP)
                .innerJoin(USER_GROUP).onKey()
                .where(USER_GROUP_MEMBERSHIP.USER_ID.eq(userId))
                .fetch { recordToData(it.into(USER_GROUP_MEMBERSHIP), UserGroupMembership::class).copy(userGroup = recordToData(it.into(USER_GROUP), UserGroup::class)) }
    }
}