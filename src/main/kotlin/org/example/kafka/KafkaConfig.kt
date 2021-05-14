package org.example.kafka

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import java.lang.invoke.MethodHandles
import java.util.*


@Configuration
class KafkaConfig(
    private val kafkaTopics: KafkaTopics,
    private val kafkaProperties: KafkaProperties,
) {

    private val logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())

    @Bean
    fun topology(): Topology {
        val streamsBuilder = StreamsBuilder()

        streamsBuilder.stream<String, String>(kafkaTopics.foo)
            .peek { key, value -> logger.info("Peeked key=$key value=$value") }
            .selectKey { _, _ -> throw RuntimeException("test-exception") }

        return streamsBuilder.build()
    }

    @Bean
    fun kStreams(topology: Topology): KafkaStreams {
        val kafkaStreams = KafkaStreams(topology, streamProperties())
        kafkaStreams.setUncaughtExceptionHandler { exception ->
            logger.warn("Uncaught exception handled", exception)
            StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse.REPLACE_THREAD
        }
        return kafkaStreams
    }

    private fun streamProperties() = Properties().apply {
        put(StreamsConfig.APPLICATION_ID_CONFIG, kafkaProperties.applicationId)
        put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.bootstrapServers)
        put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String()::class.java)
        put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String()::class.java)

    }
}
