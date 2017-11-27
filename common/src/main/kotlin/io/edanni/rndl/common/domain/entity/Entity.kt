package io.edanni.rndl.common.domain.entity

import org.threeten.bp.OffsetDateTime


interface Entity {
    val id: Long?
    val createdAt: OffsetDateTime?
    val updatedAt: OffsetDateTime?
}