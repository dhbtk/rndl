package io.edanni.rndl.common.domain.entity

import org.threeten.bp.OffsetDateTime

data class UserGroup(
        // Database
        override val id: Long? = null,
        override val createdAt: OffsetDateTime? = null,
        override val updatedAt: OffsetDateTime? = null,
        val ownerId: Long? = null,
        val name: String? = null,
        // Entity
        val owner: User? = null,
        val userGroupMemberships: List<UserGroupMembership> = emptyList()
) : Entity