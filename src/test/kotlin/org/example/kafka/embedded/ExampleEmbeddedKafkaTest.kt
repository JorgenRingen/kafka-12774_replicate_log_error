package org.example.kafka.embedded

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.Serdes
import org.example.kafka.embedded.AbstractEmbeddedKafkaTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.test.EmbeddedKafkaBroker
import java.util.*


class ExampleEmbeddedKafkaTest : AbstractEmbeddedKafkaTest() {

    @Test
    fun test() {
        val producer = KafkaProducer<String, String>(Properties().apply {
            this[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaProperties.bootstrapServers
            this[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = Serdes.StringSerde().serializer().javaClass
            this[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = Serdes.StringSerde().serializer().javaClass
        })

        producer.send(ProducerRecord(kafkaTopics.foo, "key1", "value1"))
        Thread.sleep(5000)
    }
}