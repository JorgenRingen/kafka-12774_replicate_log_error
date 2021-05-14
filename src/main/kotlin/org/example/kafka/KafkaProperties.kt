package org.example.kafka

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "kafka", ignoreUnknownFields = true)
data class KafkaProperties(
    val applicationId: String,
    val bootstrapServers: String,
    val schemaRegistryUrl: String
)