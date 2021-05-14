package org.example.kafka

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "topic", ignoreUnknownFields = true)
data class KafkaTopics(

    val foo: String,
)