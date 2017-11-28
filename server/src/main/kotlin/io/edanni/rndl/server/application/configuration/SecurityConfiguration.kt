package io.edanni.rndl.server.application.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository

@Configuration
class SecurityConfiguration {

    @Bean
    fun filterChain(http: ServerHttpSecurity) =
            http.authorizeExchange()
                    .pathMatchers("/api/upload").permitAll()
                    .anyExchange().authenticated()
                    .and()
                    .csrf().disable()
                    .formLogin().disable()
                    .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                    .build()!!
}