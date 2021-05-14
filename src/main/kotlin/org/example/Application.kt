package org.example

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.PropertySource


@SpringBootApplication
@PropertySource(
    value = ["file:/etc/config/sb-config.yaml"],
    ignoreResourceNotFound = true
)
@ConfigurationPropertiesScan
class Application { // <<replace-me>>

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Application::class.java, *args)
        }
    }
}
