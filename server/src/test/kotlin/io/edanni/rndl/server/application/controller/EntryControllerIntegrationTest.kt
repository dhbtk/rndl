package io.edanni.rndl.server.application.controller

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.edanni.rndl.server.ServerApplicationTests
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import java.io.File
import java.io.FileOutputStream
import java.util.logging.Logger
import java.util.zip.ZipFile

@Sql("/dataset/entry-controller/ingestion-test.sql")
class EntryControllerIntegrationTest : ServerApplicationTests() {
    val client: WebClient = WebClient.create()
    val log = Logger.getLogger(this::class.java.name)

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `ingestion of real data`() {
        val tempZipFile = File.createTempFile("requests", "zip")
        tempZipFile.deleteOnExit()
        javaClass.getResourceAsStream("/requests.zip").use { input ->
            FileOutputStream(tempZipFile).use { output ->
                val buffer = ByteArray(1024)
                var read = input.read(buffer)
                while (read != -1) {
                    output.write(buffer, 0, read)
                    read = input.read(buffer)
                }
            }
        }
        val zip = ZipFile(tempZipFile)
        val entries = zip.entries().toList().map { objectMapper.readValue<Map<String, String>>(zip.getInputStream(it), object : TypeReference<Map<String, String>>() {}) }
        log.info("${entries.size} entries")
        val startTime = System.currentTimeMillis()
        entries.forEach { entry ->
            val request = client.get().uri("http://localhost:8081/api/upload?" +
                    entry.map { (k, v) -> "$k=$v" }.reduce { a, c -> "$a&$c" })
            val pair = request.exchange().flatMap { response ->
                response.bodyToMono<String>().map { body ->
                    Pair(response, body)
                }
            }.toFuture().get()
            val (response, body) = pair
            assertEquals(200, response.statusCode().value())
            assertEquals("OK!", body)
        }
        val duration = (System.currentTimeMillis().toDouble() - startTime) / 1000
        log.info("${entries.size} entries processed in $duration seconds (${duration / entries.size} per entry)")
    }
}