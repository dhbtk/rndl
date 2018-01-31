package io.edanni.rndl.server.infrastructure.mapping

import io.edanni.rndl.common.domain.entity.FuelType
import io.edanni.rndl.common.domain.entity.Refueling
import io.edanni.rndl.jooq.tables.records.RefuelingRecord
import org.junit.Assert.assertEquals
import org.junit.Test

class DataClassMapperKtTest {
    @Test
    fun recordToData() {
        val record = RefuelingRecord()
        record.fuelType = "GASOLINE"
        val entity = recordToData(record, Refueling::class)
        assertEquals(FuelType.GASOLINE, entity.fuelType)
    }

}