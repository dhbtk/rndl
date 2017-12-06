package io.edanni.rndl.server.infrastructure.pagination

data class PageRequest(val page: Int? = null, val size: Int = 20)