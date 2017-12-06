package io.edanni.rndl.server.infrastructure.pagination

data class Page<out T>(val total: Int, val page: Int, val size: Int, val totalPages: Int, val content: List<T>)