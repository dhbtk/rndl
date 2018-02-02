package io.edanni.rndl.common.domain.entity

import org.threeten.bp.OffsetDateTime
import java.math.BigDecimal

data class Vehicle(
        // Database
        override val id: Long? = null,
        override val createdAt: OffsetDateTime? = null,
        override val updatedAt: OffsetDateTime? = null,
        val name: String? = null,
        val torqueId: String? = null,
        val licensePlate: String? = null,
        val gearReferenceSpeeds: Array<BigDecimal> = emptyArray(),
        val userGroupId: Long? = null,
        // Entity
        val latestEntry: Entry? = null,
        val userGroup: UserGroup? = null
) : Entity