package io.edanni.rndl.server.application.dto

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
        kff1001: Int? = null,
        kc: Int? = null,
        kff1206: BigDecimal? = null,
        kd: Int? = null,
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
    val gpsSpeed: Int?
    val rpm: Int?
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
        speed = kd
        throttle = k11
        instantEconomy = kff1203
        fuelFlow = kff125a
        fuelUsed = kff1271
    }
}