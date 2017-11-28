package io.edanni.rndl.server.infrastructure.repository

import kotlin.reflect.KClass

class RecordNotFoundException(entity: KClass<*>, field: String, value: Any?) : RuntimeException("Could not find a ${entity.simpleName} with ${field} ${value}.") {
    constructor(entity: KClass<*>, value: Any?) : this(entity, "id", value)
}