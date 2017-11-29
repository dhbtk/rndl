package io.edanni.rndl.server.application.dto

import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory
import com.vividsolutions.jts.geom.PrecisionModel
import io.edanni.rndl.common.domain.entity.Entry
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.math.BigDecimal

/**
 * This is the GET request Torque sends to remote servers.
 */
class TorqueEntryData(
        id: String? = null,
        eml: String? = null,
        session: Long? = null,
        time: Long? = null,
        kff1005: BigDecimal? = null,
        kff1006: BigDecimal? = null,
        kff1001: BigDecimal? = null,
        kc: BigDecimal? = null,
        kff1206: BigDecimal? = null,
        kd: BigDecimal? = null,
        k11: BigDecimal? = null,
        kff1203: BigDecimal? = null,
        kff125a: BigDecimal? = null,
        kff1271: BigDecimal? = null

) {
    val torqueId: String?
    val email: String?
    val tripTimestamp: Long?
    val deviceTimestamp: Long?
    val longitude: BigDecimal?
    val latitude: BigDecimal?
    val gpsSpeed: BigDecimal?
    val rpm: BigDecimal?
    val economy: BigDecimal?
    val instantEconomy: BigDecimal?
    val speed: Int?
    val throttle: BigDecimal?
    val fuelFlow: BigDecimal?
    val fuelUsed: BigDecimal?

    /**
     * The fields have these weird names because Spring dooesn't let
     * you use @QueryParam and related annotations for injected beans. :(
     */
    init {
        torqueId = id
        email = eml
        tripTimestamp = session
        deviceTimestamp = time
        longitude = kff1005
        latitude = kff1006
        gpsSpeed = kff1001
        rpm = kc
        economy = kff1206
        speed = kd?.intValueExact()
        throttle = k11
        instantEconomy = kff1203
        fuelFlow = kff125a
        fuelUsed = kff1271
    }

    fun toEntry(tripId: Long) = Entry(
            tripId = tripId,
            deviceTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(deviceTimestamp!!), ZoneId.systemDefault()),
            coordinates = if (longitude != null && latitude != null) {
                GeometryFactory(PrecisionModel(), 4326).createPoint(Coordinate(longitude.toDouble(), latitude.toDouble()))
            } else {
                null
            },
            gpsSpeed = gpsSpeed,
            gpsAltitude = null,
            rpm = rpm,
            economy = economy,
            speed = speed,
            throttle = throttle,
            instantEconomy = instantEconomy,
            fuelFlow = fuelFlow,
            fuelUsed = fuelUsed
    )
}