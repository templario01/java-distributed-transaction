package templario01.io.antifraud.adapter.config;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.SenderOptions;

import java.util.Collections;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public ReactiveKafkaProducerTemplate<String, Object> reactiveKafkaProducerTemplate(
            KafkaProperties properties) {

        Map<String, Object> props = properties.buildProducerProperties();

        return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(props));
    }

    @Bean
    public ReceiverOptions<String, Object> kafkaReceiverOptions(
            KafkaProperties properties) {

        String topic = "payment.transaction";
        Map<String, Object> props = properties.buildConsumerProperties();

        return ReceiverOptions.<String, Object>create(props)
                .subscription(Collections.singleton(topic));
    }

    @Bean
    public ReactiveKafkaConsumerTemplate<String, Object> reactiveKafkaConsumerTemplate(
            ReceiverOptions<String, Object> kafkaReceiverOptions) {
        return new ReactiveKafkaConsumerTemplate<>(kafkaReceiverOptions);
    }
}

