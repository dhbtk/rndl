package io.edanni.rndl.common.domain.entity

import org.threeten.bp.OffsetDateTime

data class UserGroupMembership(
        override val id: Long? = null,
        override val createdAt: OffsetDateTime? = null,
        override val updatedAt: OffsetDateTime? = null,
        val userId: Long? = null,
        val userGroupId: Long? = null,
        val administrator: Boolean? = null,
        // Entity
        val user: User? = null,
        val userGroup: UserGroup? = null
) : Entity