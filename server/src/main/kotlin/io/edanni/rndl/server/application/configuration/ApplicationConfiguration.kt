package io.edanni.rndl.server.application.configuration

import com.bedatadriven.jackson.datatype.jts.JtsModule
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.threetenbp.ThreeTenModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository

@Configuration
class ApplicationConfiguration : Jackson2ObjectMapperBuilderCustomizer {
    override fun customize(jacksonObjectMapperBuilder: Jackson2ObjectMapperBuilder?) {
        jacksonObjectMapperBuilder!!
                .modulesToInstall(KotlinModule(), JtsModule(), ThreeTenModule())
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }

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