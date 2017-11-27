package io.edanni.rndl.common.domain.entity

import com.vividsolutions.jts.geom.Geometry
import org.threeten.bp.LocalDateTime
import org.threeten.bp.OffsetDateTime
import java.math.BigDecimal

data class Entry(
        override val id: Long? = null,
        override val createdAt: OffsetDateTime? = null,
        override val updatedAt: OffsetDateTime? = null,
        val tripId: Long? = null,
        val deviceTime: LocalDateTime? = null,
        val coordinates: Geometry? = null,
        val gpsSpeed: BigDecimal? = null,
        val gpsAltitude: BigDecimal? = null,
        val rpm: BigDecimal? = null,
        val economy: BigDecimal? = null,
        val speed: Int? = null,
        val throttle: BigDecimal? = null,
        val instantEconomy: BigDecimal? = null,
        val fuelFlow: BigDecimal? = null,
        val fuelUsed: BigDecimal? = null
) : Entity