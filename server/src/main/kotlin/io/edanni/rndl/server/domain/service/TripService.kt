package io.edanni.rndl.server.domain.service

import io.edanni.rndl.common.application.dto.GroupedTripList
import io.edanni.rndl.common.domain.entity.User
import io.edanni.rndl.server.domain.repository.TripRepository
import org.springframework.stereotype.Service

@Service
class TripService(private val tripRepository: TripRepository) {
    //
    // CONTROLLER ENTRY POINTS
    //

    fun listTripsFiltered(year: Int, month: Int, user: User, vehicleId: Long?): List<GroupedTripList> {
        return tripRepository.listTripsFiltered(year, month, user, vehicleId)
                .groupBy { it.startTime!!.toLocalDate() }.entries.map { (date, trips) -> GroupedTripList(date, trips) }
                .sortedWith(Comparator { a, b -> a.date.compareTo(b.date) }).reversed()
    }

    fun findByIdAndUser(id: Long, user: User) = tripRepository.findByIdAndUser(id, user)
}