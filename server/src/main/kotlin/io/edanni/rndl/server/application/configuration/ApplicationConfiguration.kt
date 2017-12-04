package io.edanni.rndl.server.application.configuration

import com.bedatadriven.jackson.datatype.jts.JtsModule
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.threetenbp.ThreeTenModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.edanni.rndl.server.infrastructure.jwt.JwtTokenSecurityProvider
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers

@Configuration
@EnableCaching
class ApplicationConfiguration : Jackson2ObjectMapperBuilderCustomizer {
    override fun customize(jacksonObjectMapperBuilder: Jackson2ObjectMapperBuilder?) {
        jacksonObjectMapperBuilder!!
                .modulesToInstall(KotlinModule(), JtsModule(), ThreeTenModule())
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }

    @Bean
    fun filterChain(http: ServerHttpSecurity, jwtTokenSecurityProvider: JwtTokenSecurityProvider): SecurityWebFilterChain {
        return http.authorizeExchange()
                .pathMatchers("/api/upload").permitAll()
                .pathMatchers(HttpMethod.POST, "/api/token").permitAll()
                .anyExchange().authenticated()
                .and()
                .csrf().disable()
                .formLogin()
                .requiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/api/token"))
                .authenticationSuccessHandler(jwtTokenSecurityProvider)
                .authenticationFailureHandler(jwtTokenSecurityProvider)
                .securityContextRepository(jwtTokenSecurityProvider)
                .authenticationEntryPoint(jwtTokenSecurityProvider)
                .and()
                .logout()
                .requiresLogout(ServerWebExchangeMatchers.pathMatchers(HttpMethod.DELETE, "/api/token"))
                .logoutHandler(jwtTokenSecurityProvider)
                .logoutSuccessHandler(jwtTokenSecurityProvider)
                .and()
                .securityContextRepository(jwtTokenSecurityProvider)
                .build()!!
    }
}