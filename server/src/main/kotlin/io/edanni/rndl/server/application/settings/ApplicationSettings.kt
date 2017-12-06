package io.edanni.rndl.server.application.settings

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("rndl")
class ApplicationSettings {
    lateinit var jwtSecret: String
}