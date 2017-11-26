package io.edanni.rndl.server


import org.springframework.boot.SpringApplication
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class ServerApplication {
}

fun main(args: Array<String>) {
    val application = SpringApplication(ServerApplication::class.java)
    application.webApplicationType = WebApplicationType.REACTIVE
    application.run(*args)
}