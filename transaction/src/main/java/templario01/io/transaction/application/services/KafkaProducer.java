package templario01.io.transaction.application.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class KafkaProducer {
    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
    private final ReactiveKafkaProducerTemplate<String, Object> kafkaTemplate;

    public KafkaProducer(
            ReactiveKafkaProducerTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public <T> Mono<Void> sendMessage(T data, String topic) {
        String key = UUID.randomUUID().toString();

        return kafkaTemplate.send(topic, key, data)
                .doOnSuccess(result -> log.info("Mensaje enviado a Kafka con clave {}: {}", key, data))
                .doOnError(e -> log.error("Error al enviar mensaje a Kafka con clave {}: {}", key, e.getMessage(), e))
                .then();
    }
}