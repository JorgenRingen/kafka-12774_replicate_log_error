package org.example.health

import org.apache.kafka.streams.KafkaStreams
import org.slf4j.LoggerFactory
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.stereotype.Component
import java.lang.invoke.MethodHandles

@Component
class KafkaStreamsHealthIndicator constructor(
    private val kafkaStreams: KafkaStreams
) : HealthIndicator {

    private val logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())

    override fun health(): Health {
        return if (kafkaStreams.state() in listOf(
                KafkaStreams.State.CREATED,
                KafkaStreams.State.RUNNING,
                KafkaStreams.State.REBALANCING
            )
        ) {
            Health.up().build()
        } else {
            logger.warn("Kafka-streams is unhealthy!")
            Health.down().build()
        }
    }
}