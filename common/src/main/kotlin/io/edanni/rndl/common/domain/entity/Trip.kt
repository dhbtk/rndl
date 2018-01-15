package io.edanni.rndl.common.domain.entity

import org.threeten.bp.*
import java.math.BigDecimal

data class Trip(
        // Database
        override val id: Long? = null,
        override val createdAt: OffsetDateTime? = null,
        override val updatedAt: OffsetDateTime? = null,
        val startTimestamp: Long? = null,
        val duration: LocalTime? = null,
        val distance: Double? = null,
        val averageSpeed: Int? = null,
        val maximumSpeed: Int? = null,
        val economy: Double? = null,
        val fuelUsed: BigDecimal? = null,
        val vehicleId: Long? = null,
        // Entity
        val vehicle: Vehicle? = null,
        val entries: List<Entry> = emptyList()
) : Entity {
    val startTime: LocalDateTime? = if (startTimestamp != null) {
        LocalDateTime.ofInstant(Instant.ofEpochMilli(startTimestamp), ZoneId.systemDefault())
    } else {
        null
    }
}