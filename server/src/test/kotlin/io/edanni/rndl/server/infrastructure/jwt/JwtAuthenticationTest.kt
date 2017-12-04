package io.edanni.rndl.server.infrastructure.jwt

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.edanni.rndl.server.ServerApplicationTests
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.reactive.function.client.WebClient

@Sql("/dataset/jwt-authentication/jwt-authentication.sql")
class JwtAuthenticationTest : ServerApplicationTests() {
    val client: WebClient = WebClient.create()

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `token creation, use and deletion`() {
        val request = client.post().uri("http://localhost:8081/api/token")
                .syncBody("username=eduardo@edanni.io&password=12345678")
                .header("Content-Type", "application/x-www-form-urlencoded")
        val response = request.exchange().block()!!
        assertEquals(201, response.statusCode().value())
        val bodyString = response.bodyToMono(String::class.java).block()
        val body = objectMapper.readValue<Map<String, String>>(bodyString, object : TypeReference<Map<String, String>>() {})
        assertNotNull(body)
        assertNotNull(body!!["access_token"])
        val token = body["access_token"]
        val protectedRequest = client.get().uri("http://localhost:8081/api/vehicles")
                .header("Authorization", "Bearer $token")
        val protectedResponse = protectedRequest.exchange().block()!!
        assertEquals(200, protectedResponse.statusCode().value())

        val logoutResponse = client.delete().uri("http://localhost:8081/api/token")
                .header("Authorization", "Bearer $token").exchange().block()!!
        assertEquals(204, logoutResponse.statusCode().value())
    }
}