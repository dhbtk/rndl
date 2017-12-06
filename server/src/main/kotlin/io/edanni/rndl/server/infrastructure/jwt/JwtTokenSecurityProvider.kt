package io.edanni.rndl.server.infrastructure.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.ObjectMapper
import io.edanni.rndl.server.application.settings.ApplicationSettings
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.threeten.bp.OffsetDateTime
import reactor.core.publisher.Mono
import java.util.*

@Component
class JwtTokenSecurityProvider
constructor(
        private val jwtUserRepository: JwtUserRepository,
        private val jwtTokenRepository: JwtTokenRepository,
        settings: ApplicationSettings,
        private val objectMapper: ObjectMapper
) :
        ServerAuthenticationSuccessHandler,
        ServerAuthenticationFailureHandler,
        ServerLogoutHandler,
        ServerLogoutSuccessHandler,
        ServerSecurityContextRepository,
        ServerAuthenticationEntryPoint {

    companion object {
        private val AUTHORIZATION_HEADER = "Authorization"
        private val AUTHORIZATION_TYPE = "Bearer"
    }

    private val algorithm = Algorithm.HMAC512(settings.jwtSecret)

    /**
     * Generates a 401 Unauthorized for non-authenticated responses
     */
    override fun commence(exchange: ServerWebExchange?, e: AuthenticationException?): Mono<Void> {
        return Mono.fromCallable({
            exchange!!.response.statusCode = HttpStatus.UNAUTHORIZED
            null
        })
    }

    /**
     * No op for saving the security context. We don't have a session, so we just return an empty value.
     */
    override fun save(exchange: ServerWebExchange?, context: SecurityContext?): Mono<Void> {
        return Mono.empty()
    }

    /**
     * Loads the user from the "Authorization: Bearer <xxx>" header.
     */
    override fun load(exchange: ServerWebExchange?): Mono<SecurityContext> {
        return extractTokenFromExchange(exchange)
                .flatMap { token ->
                    jwtUserRepository.loadUserFromToken(token)
                            .map {
                                UsernamePasswordAuthenticationToken(
                                        it,
                                        JWT.require(algorithm).build().verify(token),
                                        it.authorities
                                )
                            }
                }
                .map { SecurityContextImpl(it) as SecurityContext }
                .onErrorResume { Mono.empty() }
    }

    private fun extractTokenFromExchange(exchange: ServerWebExchange?) =
            Mono.justOrEmpty(exchange!!.request.headers[AUTHORIZATION_HEADER]?.first())
                    .map { it!!.replace("$AUTHORIZATION_TYPE ", "") }

    /**
     * Sends a 204 No Content response code after deleting the token.
     */
    override fun onLogoutSuccess(exchange: WebFilterExchange, authentication: Authentication?): Mono<Void> {
        return Mono.fromCallable {
            exchange.exchange.response.statusCode = HttpStatus.NO_CONTENT
            null
        }
    }

    /**
     * Logs out the user by deleting his token from the database.
     */
    override fun logout(exchange: WebFilterExchange?, authentication: Authentication?): Mono<Void> {
        return extractTokenFromExchange(exchange!!.exchange)
                .flatMap { jwtTokenRepository.deleteUserTokenByToken(it) }
    }

    /**
     * Creates a token and saves it to the database.
     */
    override fun onAuthenticationSuccess(webFilterExchange: WebFilterExchange?, authentication: Authentication?): Mono<Void> {
        return Mono.justOrEmpty(authentication?.principal as UserDetails)
                .map { Pair(it, OffsetDateTime.now().plusMonths(6)) }
                .map { (user, expiration) ->
                    Triple(user, expiration, JWT.create().withSubject(user.username).withExpiresAt(Date(expiration.toInstant().toEpochMilli())).sign(algorithm))
                }
                .doOnSuccess { (user, expiration, token) ->
                    jwtTokenRepository.insertUserTokenByToken(token, expiration, user).subscribe()
                }
                .flatMap { (_, expiration, token) ->
                    val output = HashMap<String, String>()
                    output["expires_in"] = (expiration.toInstant().epochSecond - OffsetDateTime.now().toInstant().epochSecond).toString()
                    output["access_token"] = token
                    val factory = webFilterExchange!!.exchange.response.bufferFactory()
                    webFilterExchange.exchange.response.statusCode = HttpStatus.CREATED
                    webFilterExchange.exchange.response.writeWith(Mono.fromCallable {
                        factory.wrap(objectMapper.writeValueAsString(output).toByteArray())
                    })
                }
                .map { null }
    }

    /**
     * Returns a 401 Unauthorized on token creation failure.
     */
    override fun onAuthenticationFailure(webFilterExchange: WebFilterExchange?, exception: AuthenticationException?): Mono<Void> {
        return Mono.fromCallable {
            webFilterExchange!!.exchange.response.statusCode = HttpStatus.UNAUTHORIZED
            null
        }
    }
}