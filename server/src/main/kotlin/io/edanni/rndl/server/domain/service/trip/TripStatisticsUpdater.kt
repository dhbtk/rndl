package io.edanni.rndl.server.domain.service.trip

import io.edanni.rndl.server.domain.repository.TripRepository
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.scheduler.Schedulers
import java.time.Duration
import java.util.logging.Logger

@Component
class TripStatisticsUpdater(private val tripRepository: TripRepository) {
    private val log = Logger.getLogger(this::class.qualifiedName)

    lateinit private var emitter: FluxSink<Long>

    init {
        Flux.create({ emitter: FluxSink<Long> ->
            this.emitter = emitter
        }).sample(Duration.ofSeconds(5)).subscribeOn(Schedulers.elastic()).subscribe {
            log.info("Updating trip statistics, requested id: " + it)
            try {
                tripRepository.updateCalculatedTripInfo()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun update(id: Long) {
        emitter.next(id)
    }
}