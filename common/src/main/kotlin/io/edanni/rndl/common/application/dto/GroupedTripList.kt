package io.edanni.rndl.common.application.dto

import io.edanni.rndl.common.domain.entity.Trip
import org.threeten.bp.LocalDate

data class GroupedTripList(val date: LocalDate, val trips: List<Trip>)