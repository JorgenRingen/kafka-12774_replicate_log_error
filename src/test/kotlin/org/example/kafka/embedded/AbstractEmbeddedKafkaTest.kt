package org.example.kafka.embedded

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.streams.KafkaStreams
import org.awaitility.Awaitility
import org.example.kafka.KafkaProperties
import org.example.kafka.KafkaTopics
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import java.time.Duration
import java.util.*

/**
 * Starts embedded kafka and mocked schema-registry.
 * Provides methods for creating json/avro consumer and producer.
 */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
@ContextConfiguration
@ActiveProfiles("local")
@EmbeddedKafka(
    bootstrapServersProperty = "kafka.bootstrapServers",
    controlledShutdown = true,
    topics = ["foo"]
)
@DirtiesContext
abstract class AbstractEmbeddedKafkaTest {

    @Autowired
    lateinit var kafkaProperties: KafkaProperties

    @Autowired
    lateinit var kafkaTopics: KafkaTopics

    @Autowired
    lateinit var kafkaStreams: KafkaStreams

    @BeforeEach
    fun beforeEach() {
        Awaitility.waitAtMost(Duration.ofSeconds(20))
            .alias("Waiting for kafka-streams running status")
            .until {
                kafkaStreams.state() == KafkaStreams.State.RUNNING
            }
    }
}