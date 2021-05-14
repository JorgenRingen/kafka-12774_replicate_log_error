package org.example.kafka

import org.apache.kafka.streams.KafkaStreams
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import javax.annotation.PreDestroy


@Component
class KafkaLifeCycleHandler(
    private val kafkaStreams: KafkaStreams,
) {

    @EventListener
    fun start(event: ApplicationReadyEvent) {
        kafkaStreams.start()
    }

    @PreDestroy
    fun stop() {
        kafkaStreams.close()
    }
}