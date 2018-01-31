package io.edanni.rndl.common.domain.entity

import org.threeten.bp.OffsetDateTime
import java.math.BigDecimal

data class Refueling(
        override val id: Long? = null,
        override val createdAt: OffsetDateTime? = null,
        override val updatedAt: OffsetDateTime? = null,
        val vehicleId: Long? = null,
        val date: OffsetDateTime? = null,
        val pricePerLiter: BigDecimal? = null,
        val liters: BigDecimal? = null,
        val odometer: BigDecimal? = null,
        val fuelType: FuelType? = null,
        // Entity
        val vehicle: Vehicle? = null
) : Entity